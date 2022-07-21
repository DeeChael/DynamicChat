package net.deechael.dynamichat.event.bukkit;

import net.deechael.dynamichat.api.BukkitUser;
import net.deechael.dynamichat.api.Channel;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Will be fired when a player switch the channel
 */
public final class ChannelSwitchEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    private final BukkitUser bukkitUser;
    private final Channel from;
    private final Channel to;

    public ChannelSwitchEvent(BukkitUser bukkitUser, Channel from, Channel to) {
        this.bukkitUser = bukkitUser;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public BukkitUser getUser() {
        return bukkitUser;
    }

    public Channel getFrom() {
        return from;
    }

    public Channel getTo() {
        return to;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
