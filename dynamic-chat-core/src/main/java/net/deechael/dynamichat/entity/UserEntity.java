package net.deechael.dynamichat.entity;

import me.clip.placeholderapi.PlaceholderAPI;
import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.feature.Filter;
import net.deechael.dynamichat.util.ConfigUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class UserEntity implements User {

    private ChannelEntity current = null;
    private final List<ChannelEntity> available = new ArrayList<>();
    private final CommandSender sender;

    public UserEntity(CommandSender sender) {
        this.sender = sender;
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
                sender.sendMessage(ConfigUtils.lang("message.filter-cancel"));
                sender.spigot().sendMessage(new ComponentBuilder(ConfigUtils.lang("message.filter-cancel-edit-button"))
                        .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, message)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ConfigUtils.lang("message.filter-cancel-edit-button-hover")))).create());
                return;
            } else {
                message = Filter.set(message);
            }
        }
        chat0(message);
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

    abstract void chat0(String message);

}
