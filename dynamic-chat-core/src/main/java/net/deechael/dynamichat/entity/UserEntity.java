package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.ChatManager;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.event.UserChatEvent;
import net.deechael.dynamichat.feature.Filter;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.ColorUtils;
import net.deechael.dynamichat.util.ConfigUtils;
import net.deechael.dynamichat.util.Lang;
import net.deechael.useless.objs.TriObj;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class UserEntity implements User {

    private final List<ChannelEntity> available = new ArrayList<>();
    private final CommandSender sender;
    private ChannelEntity current = null;

    public UserEntity(CommandSender sender) {
        this.sender = sender;
        this.current = (ChannelEntity) ChatManager.getManager().getGlobal();
    }

    @Override
    public void whisper(User another, String message) {

    }

    @Override
    public void say(String message) {

    }

    @Override
    public void chat(String message) {
        if (ConfigUtils.filterEnable()) {
            if (ConfigUtils.filterMode() == Filter.Mode.CANCEL) {
                if (Filter.suit(message)) {
                    UserChatEvent event = new UserChatEvent(this, getCurrent(), getFormat(), message, new ArrayList<>());
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    Lang.send(sender, "message.filter-cancel");
                    sender.spigot().sendMessage(new ComponentBuilder(Lang.lang(sender, "message.filter-cancel-edit-button"))
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Lang.lang(sender, "message.filter-cancel-edit-button-hover")))).create());
                    return;
                }
            } else {
                message = Filter.set(message);
            }
        }
        if (ConfigUtils.replaceEnable()) {
            for (Map.Entry<String, String> replace : ConfigUtils.replaces().entrySet()) {
                message = message.replace(replace.getKey(), replace.getValue());
            }
        }
        if (ConfigUtils.chatColorEnable()) {
            message = ColorUtils.processChatColor(message);
        }
        if (ConfigUtils.chatColorGradient()) {
            message = ColorUtils.processGradientColor(message);
        }
        if (ConfigUtils.mentionPlayer()) {
            List<Player> showers = new ArrayList<>();
            if (ConfigUtils.channelEnable()) {
                for (UserEntity user : getCurrent().getUsers()) {
                    if (user instanceof PlayerUserEntity) {
                        showers.add((Player) user.getSender());
                    }
                }
            } else {
                showers.addAll(Bukkit.getOnlinePlayers());
            }
            UserChatEvent event = new UserChatEvent(this, getCurrent(), getFormat(), message, new ArrayList<>(showers).stream().map(player -> ChatManager.getManager().getPlayerUser(player)).collect(Collectors.toList()));
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
            List<Player> hearers = new ArrayList<>();
            boolean everyone = false;
            if (message.contains("@Everyone ")) {
                message = message.replace("@Everyone ", "§6@Everyone §r");
                hearers.addAll(showers);
                everyone = true;
            }/* else if (message.equalsIgnoreCase("@Everyone")) {
                message = "§6@Everyone";
                hearers.addAll(showers);
                everyone = true;
            } */
            if (message.endsWith("@Everyone")) {
                message = message.substring(0, message.length() - 9) + "§6@Everyone";
                hearers.clear();
                hearers.addAll(showers);
                everyone = true;
            }
            if (!everyone) {
                for (Player shower : new ArrayList<>(showers)) {
                    if (shower.getName().equalsIgnoreCase("Everyone")) continue;
                    if (message.contains("@" + shower.getName() + " ")) {
                        String copiedMessage = message.replace("@" + shower.getName() + " ", "§6@" + shower.getName() + " §r");
                        chat0(shower, getFormat(), copiedMessage);
                        showers.remove(shower);
                        if (!hearers.contains(shower)) hearers.add(shower);
                    }
                    /* else if (message.equalsIgnoreCase("@" + shower.getName())) {
                        String copiedMessage = "§6@" + shower.getName();
                        chat0(shower, copiedMessage);
                        showers.remove(shower);
                        if (!hearers.contains(shower)) hearers.add(shower);
                    }
                    */
                    if (message.endsWith("@" + shower.getName())) {
                        chat0(shower, getFormat(), message.substring(0, message.length() - shower.getName().length() - 1) + "§6@" + shower.getName());
                        showers.remove(shower);
                        if (!hearers.contains(shower)) hearers.add(shower);
                    }
                }
            }
            for (Player player : showers) {
                chat0(player, getFormat(), message);
            }
            hearers.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F));
        } else {
            for (UserEntity user : getCurrent().getUsers()) {
                chat0(user.getSender(), getFormat(), message);
            }
        }
        Bukkit.getConsoleSender().sendMessage("§b[" + getCurrent().getDisplayName() + "§b] §r" + ConfigUtils.getChatFormat().replace("%message%", DynamicChatPlaceholder.replaceSender(sender, message)));
    }

    public String getFormat() {
        String format = ConfigUtils.getChatFormat();
        if (current.isGlobal()) {
            int priority = 0;
            for (TriObj<String, String, Integer> triObj : ConfigUtils.permissionFormat()) {
                if (triObj.getThird() > priority) {
                    if (sender.hasPermission(triObj.getFirst())) {
                        format = triObj.getSecond();
                        priority = triObj.getThird();
                    }
                }
            }
        } else {
            format = current.getFormat();
            int priority = 0;
            for (TriObj<String, String, Integer> triObj : current.getPermissionFormats()) {
                if (triObj.getThird() > priority) {
                    if (sender.hasPermission(triObj.getFirst())) {
                        format = triObj.getSecond();
                        priority = triObj.getThird();
                    }
                }
            }
        }
        return format;
    }

    @Override
    public String getName() {
        return sender.getName();
    }

    @Override
    public ChannelEntity getCurrent() {
        return this.current;
    }

    public void setCurrent(ChannelEntity current) {
        this.current = current;
    }

    @Override
    public void moveTo(Channel channel) {
        if (!channel.getUsers().contains(this))
            throw new RuntimeException("This user cannot get to the channel");
        this.current = (ChannelEntity) channel;
    }

    @Override
    public void available(Channel channel) {
        addAvailable((ChannelEntity) channel);
    }

    @Override
    public void unavailable(Channel channel) {
        this.available.remove((ChannelEntity) channel);
    }

    @Override
    public List<ChannelEntity> getAvailable() {
        return new ArrayList<>(available);
    }

    public void addAvailable(ChannelEntity channel) {
        if (!this.available.contains(channel))
            this.available.add(channel);
    }

    public CommandSender getSender() {
        return sender;
    }

    abstract void chat0(CommandSender sender, String format, String message);

}
