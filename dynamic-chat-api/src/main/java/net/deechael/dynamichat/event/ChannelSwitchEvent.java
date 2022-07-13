package net.deechael.dynamichat.event;

import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class ChannelSwitchEvent extends Event {

    private final static HandlerList handlerList = new HandlerList();

    private final User user;
    private final Channel from;
    private final Channel to;

    public ChannelSwitchEvent(User user, Channel from, Channel to) {
        this.user = user;
        this.from = from;
        this.to = to;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public User getUser() {
        return user;
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
