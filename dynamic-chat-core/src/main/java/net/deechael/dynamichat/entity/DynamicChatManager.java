package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.MessageButton;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.StringUtils;
import net.deechael.useless.objs.FiObj;
import net.deechael.useless.objs.TriObj;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicChatManager extends ChatManager {

    private final Map<CommandSender, UserEntity> userMap = new HashMap<>();
    private final List<ChannelEntity> channels = new ArrayList<>();
    private final List<TemporaryChannelEntity> temps = new ArrayList<>();
    private final ChannelEntity global = new GlobalChannelEntity();

    private final Map<String, Message> chatCaches = new HashMap<>();

    private final Map<Integer, MessageButton> buttons = new HashMap<>();

    public DynamicChatManager() {
        global.setDisplayName(ConfigUtils.globalChannelDisplayName());
    }

    public static void quit(Player player) {
        ((DynamicChatManager) ChatManager.getManager()).userMap.remove(player);
    }

    public static DynamicChatManager getChatManager() {
        return (DynamicChatManager) ChatManager.getManager();
    }

    public static String addChatCache(Message builder) {
        String key = StringUtils.random64();
        while (getChatManager().chatCaches.containsKey(key)) {
            key = StringUtils.random64();
        }
        getChatManager().chatCaches.put(key, builder);
        return key;
    }

    public static Message getChatCache(String key) {
        return getChatManager().chatCaches.get(key);
    }

    public static void reload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UserEntity user = ((UserEntity) ChatManager.getManager().getPlayerUser(player));
            user.setCurrent(DynamicChatManager.getChatManager().global);
            for (Channel channel : getChatManager().channels) {
                user.unavailable(channel);
            }
        }
        UserEntity user = ((UserEntity) ChatManager.getManager().getUser(Bukkit.getConsoleSender()));
        user.setCurrent(DynamicChatManager.getChatManager().global);
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
                    UserEntity playerUser = ((UserEntity) ChatManager.getManager().getPlayerUser(player));
                    playerUser.setCurrent(DynamicChatManager.getChatManager().global);
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

    @Override
    public UserEntity getUser(CommandSender sender) {
        if (sender instanceof Player) {
            return getPlayerUser((Player) sender);
        }
        if (!userMap.containsKey(sender)) {
            ConsoleUserEntity userEntity = new ConsoleUserEntity(sender);
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
    public PlayerUserEntity getPlayerUser(Player player) {
        if (!userMap.containsKey(player)) {
            PlayerUserEntity userEntity = new PlayerUserEntity(player);
            for (ChannelEntity entity : this.channels) {
                if (entity.isAvailable()) {
                    entity.getUsersRaw().add(userEntity);
                    userEntity.available(entity);
                }
            }
            userMap.put(player, userEntity);
        }
        return (PlayerUserEntity) userMap.get(player);
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

}
