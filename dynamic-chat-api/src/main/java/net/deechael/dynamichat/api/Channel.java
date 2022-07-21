package net.deechael.dynamichat.api;

import net.deechael.useless.objs.TriObj;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Chat channel
 */
public interface Channel {

    /**
     * broadcast the message to all users who have access to this channel
     * @param message the message to be broadcast
     */
    void broadcast(String message);

    /**
     * Get all the users who have access to the channel
     *
     * @return the users who have access to the channel
     */
    List<? extends User> getUsers();

    /**
     * Get the name of the channel
     *
     * @return the name of the channel
     */
    @NotNull
    String getName();

    /**
     * Get the display name of the channel
     *
     * @return the display name of the channel
     */
    String getDisplayName();

    /**
     * Get the chat format of the channel
     *
     * @return the format of the channel
     */
    @Nullable
    String getFormat();

    /**
     * Get the formats of whom has different permissions
     *
     * @return (permission, format, priority)
     */
    List<TriObj<String, String, Integer>> getPermissionFormats();

    /**
     * Is this channel the global channel
     *
     * @return the result
     */
    boolean isGlobal();

    /**
     * Make the channel available for all users
     *
     * @param available status
     */
    void setAvailableForAll(boolean available);

}
