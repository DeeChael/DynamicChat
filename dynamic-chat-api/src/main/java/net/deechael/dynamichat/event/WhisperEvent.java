package net.deechael.dynamichat.event;

import net.deechael.dynamichat.api.User;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WhisperEvent extends Event implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();

    private final User sender;

    private final User receiver;

    private String message;

    private boolean cancelled = false;

    public WhisperEvent(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
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
