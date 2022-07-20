package net.deechael.dynamichat.event;

import net.deechael.dynamichat.api.BukkitUser;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public final class WhisperEvent extends Event implements Cancellable {

    private final static HandlerList handlerList = new HandlerList();

    private final BukkitUser sender;

    private final BukkitUser receiver;

    private String message;

    private boolean cancelled = false;

    public WhisperEvent(BukkitUser sender, BukkitUser receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    public BukkitUser getSender() {
        return sender;
    }

    public BukkitUser getReceiver() {
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
