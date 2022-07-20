package net.deechael.dynamichat.event;

import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.api.Channel;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class UserChatEvent extends Event implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();

    private final BukkitUser bukkitUser;
    private final Channel channel;
    private final String format;
    private final String message;
    private final List<BukkitUser> recipients;
    private boolean cancelled = false;

    public UserChatEvent(BukkitUser bukkitUser, Channel channel, String format, String message, List<BukkitUser> recipients) {
        super(true);
        this.bukkitUser = bukkitUser;
        this.channel = channel;
        this.format = format;
        this.message = message;
        this.recipients = recipients;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public BukkitUser getUser() {
        return bukkitUser;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getFormat() {
        return format;
    }

    public String getMessage() {
        return message;
    }

    public List<BukkitUser> getRecipients() {
        return recipients;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
