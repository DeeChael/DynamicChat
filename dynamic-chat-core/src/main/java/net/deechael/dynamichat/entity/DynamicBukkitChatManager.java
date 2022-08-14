package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.DyChatPlugin;
import net.deechael.dynamichat.api.*;
import net.deechael.dynamichat.api.Time;
import net.deechael.dynamichat.sql.Sqlite;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.DuObj;
import net.deechael.useless.objs.FiObj;
import net.deechael.useless.objs.TriObj;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DynamicBukkitChatManager extends BukkitChatManager {

    private final Map<CommandSender, BukkitUserEntity> userMap = new HashMap<>();
    private final List<ChannelEntity> channels = new ArrayList<>();
    private final List<TemporaryChannelEntity> temps = new ArrayList<>();
    private final ChannelEntity global = new GlobalChannelEntity();

    private final Map<String, Message> chatCaches = new HashMap<>();

    private final Map<Integer, MessageButton> buttons = new HashMap<>();

    private final List<Map.Entry<String, MuteMessage>> records = new ArrayList<>();

    private final List<String> recordKeys = new ArrayList<>();

    private Sqlite sqlite;

    public DynamicBukkitChatManager() {
        global.setDisplayName(ConfigUtils.globalChannelDisplayName());
    }

    public static boolean quit(Player player) {
        return ((DynamicBukkitChatManager) BukkitChatManager.getManager()).userMap.remove(player) != null;
    }

    public static DynamicBukkitChatManager getChatManager() {
        return (DynamicBukkitChatManager) BukkitChatManager.getManager();
    }

    public static String newChatCache(BukkitUser bukkitUser, String message) {
        String key = StringUtils.random64();
        while (getChatManager().chatCaches.containsKey(key)) {
            key = StringUtils.random64();
        }
        while (getChatManager().recordKeys.contains(key)) {
            key = StringUtils.random64();
        }
        Message messageEntity = new MessageEntity(bukkitUser, message, key, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        getChatManager().recordKeys.add(key);
        getChatManager().records.add(new AbstractMap.SimpleEntry<>(key, messageEntity));
        getChatManager().chatCaches.put(key, messageEntity);
        addRecord(messageEntity);
        return key;
    }

    public static Message getChatCache(String key) {
        return getChatManager().chatCaches.get(key);
    }

    public static List<MuteMessage> getBySender(User user) {
        return new ArrayList<>(getChatManager().records).stream().filter(entry -> entry.getValue().getSenderName().equalsIgnoreCase(user.getName())).map(Map.Entry::getValue).toList();
    }

    public static String getLastMessage(User user) {
        List<MuteMessage> messages = getBySender(user);
        MuteMessage message = messages.get(messages.size() - 1);
        if (System.currentTimeMillis() - message.getSendTime().getTime() > 1000 * 60)
            return null;
        return message.getContent();
    }

    public static DuObj<String, Integer> lastMessageWithRepeatedTimes(User user) {
        List<MuteMessage> messages = getBySender(user);
        MuteMessage message = messages.get(messages.size() - 1);
        if (System.currentTimeMillis() - message.getSendTime().getTime() > 1000 * 60)
            return null;
        String content = message.getContent();
        int times = 1;
        for (int i = messages.size() - 2; i >= 0; i--) {
            MuteMessage msg = messages.get(i);
            if (!msg.getContent().equals(content))
                continue;
            if (message.getSendTime().getTime() - msg.getSendTime().getTime() > 1000 * 60)
                continue;
            times++;
            message = msg;
        }
        return new DuObj<>(content, times);
    }

    public static boolean isLastSender(String name) {
        String last = getLastSender();
        if (last == null)
            return false;
        if (!last.equalsIgnoreCase(name))
            return false;
        MuteMessage message = getMessage(messages() - 1);
        return new Date().getTime() - message.getSendTime().getTime() <= 60 * 1000L;
    }

    public static String getLastSender() {
        if (getChatManager().records.size() == 0)
            return null;
        return getChatManager().records.get(getChatManager().records.size() - 1).getValue().getSenderName();
    }

    public static void reload() {
        if (getChatManager().sqlite != null) {
            getChatManager().sqlite.close();
        }
        File dbFile = new File(DyChatPlugin.getInstance().getDataFolder(), "record.db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException ignored) {
            }
        }
        getChatManager().records.clear();
        getChatManager().recordKeys.clear();
        getChatManager().sqlite = new Sqlite(dbFile);
        getChatManager().sqlite.executeUpdate("CREATE TABLE IF NOT EXISTS `messages` ( `msg_id` TEXT , `sender` TEXT , `content` TEXT, `time` TEXT);");
        ResultSet resultSet = getChatManager().sqlite.executeQuery("SELECT * FROM `messages`;");
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("msg_id");
                String sender = resultSet.getString("sender");
                String content = resultSet.getString("content");
                String time = resultSet.getString("time");
                getChatManager().recordKeys.add(id);
                getChatManager().records.add(new AbstractMap.SimpleEntry<>(id, new MuteMessageEntity(sender, content, id, time)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            BukkitUserEntity user = ((BukkitUserEntity) BukkitChatManager.getManager().getBukkitPlayerUser(player));
            user.setCurrent(DynamicBukkitChatManager.getChatManager().global);
            for (Channel channel : getChatManager().channels) {
                user.unavailable(channel);
            }
        }
        BukkitUserEntity user = ((BukkitUserEntity) BukkitChatManager.getManager().getBukkitUser(Bukkit.getConsoleSender()));
        user.setCurrent(DynamicBukkitChatManager.getChatManager().global);
        for (Channel channel : getChatManager().channels) {
            user.unavailable(channel);
        }
        getChatManager().channels.clear();
        getChatManager().temps.forEach(TemporaryChannelEntity::drop);
        for (FiObj<String, String, Boolean, String, List<TriObj<String, String, Integer>>> channel : ConfigUtils.channels()) {
            ChannelEntity entity = new ChannelEntity(channel.getFirst());
            entity.setAvailable(channel.getThird());
            if (channel.getSecond() != null) {
                entity.setDisplayName(channel.getSecond());
            }
            if (entity.isAvailable()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BukkitUserEntity playerUser = ((BukkitUserEntity) BukkitChatManager.getManager().getBukkitPlayerUser(player));
                    playerUser.setCurrent(DynamicBukkitChatManager.getChatManager().global);
                    playerUser.available(entity);
                    entity.getUsersRaw().add(playerUser);
                }
                entity.getUsersRaw().add(user);
                user.available(entity);
            }
            entity.setFormat(channel.getFourth());
            entity.setPermissionFormats(channel.getFifth());
            getChatManager().channels.add(entity);
        }
    }

    public static void addRecord(MuteMessage message) {
        PreparedStatement preparedStatement = getChatManager().sqlite.preparedStatement("INSERT INTO `messages` (`msg_id`, `sender`, `content`, `time`) VALUES (?, ?, ?, ?);");
        try {
            preparedStatement.setString(1, message.getId());
            preparedStatement.setString(2, message.getSenderName());
            preparedStatement.setString(3, message.getContent());
            preparedStatement.setString(4, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getSendTime()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getMessageIndex(String id) {
        return getChatManager().recordKeys.indexOf(id);
    }

    public static int messages() {
        return getChatManager().recordKeys.size();
    }

    public static MuteMessage getMessage(int index) {
        return getChatManager().records.get(index).getValue();
    }

    @Override
    public BukkitUserEntity getBukkitUser(CommandSender sender) {
        if (sender instanceof Player) {
            return getBukkitPlayerUser((Player) sender);
        }
        if (!userMap.containsKey(sender)) {
            ConsoleBukkitUserEntity userEntity = new ConsoleBukkitUserEntity(sender);
            for (ChannelEntity entity : this.channels) {
                if (entity.isAvailable()) {
                    entity.getUsersRaw().add(userEntity);
                    userEntity.available(entity);
                }
            }
            userMap.put(sender, userEntity);
        }
        return userMap.get(sender);
    }

    @Override
    public PlayerBukkitUserEntity getBukkitPlayerUser(Player player) {
        if (!userMap.containsKey(player)) {
            PlayerBukkitUserEntity userEntity = new PlayerBukkitUserEntity(player);
            for (ChannelEntity entity : this.channels) {
                if (entity.isAvailable()) {
                    entity.getUsersRaw().add(userEntity);
                    userEntity.available(entity);
                }
            }
            userMap.put(player, userEntity);
        }
        return (PlayerBukkitUserEntity) userMap.get(player);
    }

    @Override
    public ChannelEntity getChannel(String name) {
        for (ChannelEntity channel : this.channels) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<ChannelEntity> getChannels() {
        return new ArrayList<>(this.channels);
    }

    @Override
    public List<TemporaryChannelEntity> getTemporaryChannels() {
        return new ArrayList<>(this.temps);
    }

    @Override
    public Channel getGlobal() {
        return this.global;
    }

    @Override
    public TemporaryChannelEntity createTemporaryChannel(@Nullable String displayName, @Nullable String format) {
        TemporaryChannelEntity channel = new TemporaryChannelEntity(displayName, format);
        this.temps.add(channel);
        return channel;
    }

    @Override
    public void registerButton(int index, MessageButton button) {
        if (this.buttons.containsKey(index))
            throw new RuntimeException("This is index has been registered");
        this.buttons.put(index, button);
    }

    @Override
    public Map<Integer, MessageButton> getButtons() {
        return new HashMap<>(buttons);
    }

    @Override
    public int getButtonMaxIndex() {
        int index = -1;
        for (int i : buttons.keySet()) {
            if (i > index) {
                index = i;
            }
        }
        return index;
    }

    @Override
    public boolean languageFollowClient() {
        return ConfigUtils.languageFollowClient();
    }

    @Override
    public String getDefaultLanguage() {
        return ConfigUtils.lang();
    }

    @Override
    public int getIndex(MuteMessage message) {
        if (!recordKeys.contains(message.getId()))
            return -1;
        return recordKeys.indexOf(message.getId());
    }

    @Override
    public MuteMessage getMessageByIndex(int index) {
        if (index >= records.size())
            throw new IndexOutOfBoundsException("Cannot find the message");
        return records.get(index).getValue();
    }

    @Override
    public MuteMessage getMessageById(String id) {
        if (!recordKeys.contains(id))
            throw new RuntimeException("Cannot find the message");
        return records.get(recordKeys.indexOf(id)).getValue();
    }

    @Override
    public Context getContext(int startIndex, int endIndex) {
        if (startIndex > endIndex)
            throw new RuntimeException("End index cannot be lower than start index");
        if (!(startIndex >= 0))
            throw new IndexOutOfBoundsException("Index cannot be lower than 0");
        if (startIndex >= records.size())
            throw new IndexOutOfBoundsException("Cannot locate the context");
        if (endIndex >= records.size())
            throw new IndexOutOfBoundsException("Cannot locate the context");
        List<MuteMessage> messages = new ArrayList<>();
        for (int i = startIndex; i < endIndex; i++) {
            messages.add(records.get(i).getValue());
        }
        return new ContextEntity(messages);
    }

    @Override
    public Context getContext(MuteMessage startMessage, int amount) {
        int index = getIndex(startMessage);
        return this.getContext(index, index + amount);
    }

    @Override
    public int recordedMessages() {
        return records.size();
    }

    @Override
    public Time createTime(int years, int months, int weeks, int days, int hours, int minutes, int seconds) {
        return new TimeEntity(years, months, weeks, days, hours, minutes, seconds);
    }

    @Override
    public Time createTime(long seconds) {
        return new TimeEntity(seconds);
    }

    @Override
    public Time parseTime(String timeString) {
        return TimeEntity.of(timeString);
    }

    @Override
    public BanIPManager getBanIPManager() {
        return DynamicBanIPManager.INSTANCE;
    }

}
