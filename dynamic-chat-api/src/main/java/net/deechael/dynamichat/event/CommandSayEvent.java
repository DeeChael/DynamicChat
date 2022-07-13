package net.deechael.dynamichat.event;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class CommandSayEvent extends Event implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();

    private final User user;
    private final Channel channel;
    private final List<User> recipients;
    private String format;
    private String message;
    private boolean cancelled = false;

    public CommandSayEvent(User user, Channel channel, String format, String message, List<User> recipients) {
        this.user = user;
        this.channel = channel;
        this.format = format;
        this.message = message;
        this.recipients = recipients;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public List<User> getRecipients() {
        return recipients;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }


}
