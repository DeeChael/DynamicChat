package net.deechael.dynamichat.entity;

import me.clip.placeholderapi.PlaceholderAPI;
import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.event.bukkit.CommandSayEvent;
import net.deechael.dynamichat.event.bukkit.UserChatEvent;
import net.deechael.dynamichat.event.bukkit.WhisperEvent;
import net.deechael.dynamichat.feature.Filter;
import net.deechael.dynamichat.placeholder.DynamicChatPlaceholder;
import net.deechael.dynamichat.util.*;
import net.deechael.useless.objs.DuObj;
import net.deechael.useless.objs.TriObj;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class BukkitUserEntity implements BukkitUser {

    private final List<ChannelEntity> available = new ArrayList<>();
    private final CommandSender sender;
    private ChannelEntity current = null;

    public BukkitUserEntity(CommandSender sender) {
        this.sender = sender;
        this.current = (ChannelEntity) BukkitChatManager.getManager().getGlobal();
    }

    @Override
    public void sendMessage(String message) {
        this.getSender().sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent... components) {
        this.getSender().spigot().sendMessage(components);
    }

    @Override
    public void sendMessage(ComponentBuilder builder) {
        this.getSender().spigot().sendMessage(builder.create());
    }

    @Override
    public void whisper(BukkitUser another, String message) {
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
        WhisperEvent event = new WhisperEvent(this, another, message);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        message = event.getMessage();
        this.getSender().sendMessage(DynamicChatPlaceholder.replaceReceiver(another.getSender(), ConfigUtils.getWhisperSend()).replace("%message%", message));
        another.getSender().sendMessage(DynamicChatPlaceholder.replaceSender(this.getSender(), ConfigUtils.getWhisperReceive()).replace("%message%", message));
        if (ConfigUtils.whisperSound()) {
            CommandSender s = another.getSender();
            if (s instanceof Player player) {
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void say(String message) {
        ChatColor chattingColor = null;
        if (this instanceof PlayerBukkitUserEntity) {
            if (ConfigUtils.chatColorChangeable()) {
                chattingColor = PlayerUtils.color(((PlayerBukkitUserEntity) this).getSender());
            }
        }
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
        List<Player> showers = new ArrayList<>(Bukkit.getOnlinePlayers());
        CommandSayEvent event = new CommandSayEvent(this, getCurrent(), ConfigUtils.getSayFormat(), message, new ArrayList<>(showers).stream().map(player -> BukkitChatManager.getManager().getBukkitPlayerUser(player)).collect(Collectors.toList()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        String format = event.getFormat();
        message = event.getMessage();
        if (!ConfigUtils.allowSpam()) {
            DuObj<String, Integer> lastMessageWithTimes = DynamicBukkitChatManager.lastMessageWithRepeatedTimes(this);
            if (lastMessageWithTimes != null) {
                if (message.equals(lastMessageWithTimes.getFirst())) {
                    if (lastMessageWithTimes.getSecond() + 1 >= ConfigUtils.maxSpamLimit()) {
                        Lang.send(this.getSender(), "message.spam", ConfigUtils.maxSpamLimit());
                        return;
                    }
                }
            }
        }
        showers.clear();
        showers.addAll(event.getRecipients().stream().map(user -> ((PlayerBukkitUserEntity) user).getSender()).toList());
        String msgId = DynamicBukkitChatManager.newChatCache(this, message);
        if (ConfigUtils.mentionPlayer()) {
            List<Player> hearers = new ArrayList<>();
            boolean everyoneMessageFormatted = false;
            if (message.contains("@Everyone ")) {
                everyoneMessageFormatted = true;
                hearers.addAll(showers);
            }/* else if (message.equalsIgnoreCase("@Everyone")) {
                message = "§6@Everyone";
                hearers.addAll(showers);
                everyone = true;
            } */
            if (message.endsWith("@Everyone")) {
                everyoneMessageFormatted = true;
                hearers.clear();
                hearers.addAll(showers);
            }
            for (Player shower : new ArrayList<>(showers)) {
                if (shower.getName().equalsIgnoreCase("Everyone")) continue;
                if (message.contains("@" + shower.getName() + " ")) {
                    String copiedMessage = message;
                    if (everyoneMessageFormatted) {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMentionPlayerAndAll(copiedMessage, shower);
                        }
                    } else {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMention(copiedMessage, shower);
                        }
                    }
                    copiedMessage = copiedMessage.replace("@" + shower.getName() + " ", "§6@" + shower.getName() + " §r").replace("@Everyone ", "§6@Everyone §r");
                    if (copiedMessage.endsWith("@Everyone")) {
                        copiedMessage = copiedMessage.substring(0, copiedMessage.length() - 9) + "§6@Everyone";
                    } else if (copiedMessage.endsWith("@" + shower.getName())) {
                        copiedMessage = copiedMessage.substring(0, copiedMessage.length() - shower.getName().length() - 1) + "§6@" + shower.getName();
                    }
                    chat0(shower, format, copiedMessage, msgId);
                    showers.remove(shower);
                    if (!hearers.contains(shower)) hearers.add(shower);
                } else if (message.endsWith("@" + shower.getName())) {
                    String copiedMessage = message;
                    if (everyoneMessageFormatted) {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMentionPlayerAndAll(copiedMessage, shower);
                        }
                    } else {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMention(copiedMessage, shower);
                        }
                    }
                    copiedMessage = copiedMessage.substring(0, copiedMessage.length() - shower.getName().length() - 1) + "§6@" + shower.getName().replace("@Everyone ", "§6@Everyone §r");
                    chat0(shower, format, copiedMessage, msgId);
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

            }
            if (everyoneMessageFormatted) {
                if (chattingColor != null) {
                    message = chattingColor.applyWithMentionAll(message);
                }
                if (message.endsWith("@Everyone")) {
                    message = message.substring(0, message.length() - 9) + "§6@Everyone";
                }
                message = message.replace("@Everyone ", "§6@Everyone §r");
            } else {
                if (chattingColor != null) {
                    message = chattingColor.apply(message);
                }
            }
            for (Player player : showers) {
                chat0(player, format, message, msgId);
            }
            hearers.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F));
        } else {
            if (this instanceof PlayerBukkitUserEntity) {
                if (chattingColor != null) {
                    message = chattingColor.apply(message);
                }
            }
            for (Player shower : showers) {
                chat0(shower, format, message, msgId);
            }
        }
    }

    @Override
    public void chat(String message) {
        ChatColor chattingColor = null;
        if (this instanceof PlayerBukkitUserEntity) {
            chattingColor = PlayerUtils.color(((PlayerBukkitUserEntity) this).getSender());
        }
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
        List<Player> showers = new ArrayList<>();
        if (ConfigUtils.channelEnable()) {
            for (BukkitUserEntity user : getCurrent().getUsers()) {
                if (user instanceof PlayerBukkitUserEntity) {
                    showers.add((Player) user.getSender());
                }
            }
        } else {
            showers.addAll(Bukkit.getOnlinePlayers());
        }
        UserChatEvent event = new UserChatEvent(this, getCurrent(), getFormat(), message, new ArrayList<>(showers).stream().map(player -> BukkitChatManager.getManager().getBukkitPlayerUser(player)).collect(Collectors.toList()));
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        String format = event.getFormat();
        message = event.getMessage();
        showers.clear();
        showers.addAll(event.getRecipients().stream().map(user -> ((PlayerBukkitUserEntity) user).getSender()).toList());
        String msgId = DynamicBukkitChatManager.newChatCache(this, message);
        if (ConfigUtils.mentionPlayer()) {
            List<Player> hearers = new ArrayList<>();
            boolean everyoneMessageFormatted = false;
            if (message.contains("@Everyone ")) {
                everyoneMessageFormatted = true;
                hearers.addAll(showers);
            }/* else if (message.equalsIgnoreCase("@Everyone")) {
                message = "§6@Everyone";
                hearers.addAll(showers);
                everyone = true;
            } */
            if (message.endsWith("@Everyone")) {
                everyoneMessageFormatted = true;
                hearers.clear();
                hearers.addAll(showers);
            }
            for (Player shower : new ArrayList<>(showers)) {
                if (shower.getName().equalsIgnoreCase("Everyone")) continue;
                if (message.contains("@" + shower.getName() + " ")) {
                    String copiedMessage = message;
                    if (everyoneMessageFormatted) {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMentionPlayerAndAll(copiedMessage, shower);
                        }
                    } else {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMention(copiedMessage, shower);
                        }
                    }
                    copiedMessage = copiedMessage.replace("@" + shower.getName() + " ", "§6@" + shower.getName() + " §r").replace("@Everyone ", "§6@Everyone §r");
                    if (copiedMessage.endsWith("@Everyone")) {
                        copiedMessage = copiedMessage.substring(0, copiedMessage.length() - 9) + "§6@Everyone";
                    } else if (copiedMessage.endsWith("@" + shower.getName())) {
                        copiedMessage = copiedMessage.substring(0, copiedMessage.length() - shower.getName().length() - 1) + "§6@" + shower.getName();
                    }
                    chat0(shower, format, copiedMessage, msgId);
                    showers.remove(shower);
                    if (!hearers.contains(shower)) hearers.add(shower);
                } else if (message.endsWith("@" + shower.getName())) {
                    String copiedMessage = message;
                    if (everyoneMessageFormatted) {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMentionPlayerAndAll(copiedMessage, shower);
                        }
                    } else {
                        if (chattingColor != null) {
                            copiedMessage = chattingColor.applyWithMention(copiedMessage, shower);
                        }
                    }
                    copiedMessage = copiedMessage.substring(0, copiedMessage.length() - shower.getName().length() - 1) + "§6@" + shower.getName().replace("@Everyone ", "§6@Everyone §r");
                    chat0(shower, format, copiedMessage, msgId);
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

            }
            if (everyoneMessageFormatted) {
                if (chattingColor != null) {
                    message = chattingColor.applyWithMentionAll(message);
                }
                if (message.endsWith("@Everyone")) {
                    message = message.substring(0, message.length() - 9) + "§6@Everyone";
                }
                message = message.replace("@Everyone ", "§6@Everyone §r");
            } else {
                if (chattingColor != null) {
                    message = chattingColor.apply(message);
                }
            }
            for (Player player : showers) {
                chat0(player, format, message, msgId);
            }
            hearers.forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F));
        } else {
            if (this instanceof PlayerBukkitUserEntity) {
                if (chattingColor != null) {
                    message = chattingColor.apply(message);
                }
            }
            for (Player player : showers) {
                chat0(player, format, message, msgId);
            }
        }
        if (this instanceof PlayerBukkitUserEntity) {
            message = PlaceholderAPI.setPlaceholders(((PlayerBukkitUserEntity) this).getSender(), message);
        }
        Bukkit.getConsoleSender().sendMessage(DynamicChatPlaceholder.replaceSender(sender, "§b[" + getCurrent().getDisplayName() + "§b] §r" + ConfigUtils.getChatFormat().replace("%message%", message)));
    }

    public String getFormat() {
        if (DynamicBukkitChatManager.isLastSender(this.getName()) && ConfigUtils.foldMessageEnable())
            return ConfigUtils.foldMessageFormat();
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

    abstract void chat0(CommandSender sender, String format, String message, String id);

}
