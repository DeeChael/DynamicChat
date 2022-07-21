package net.deechael.dynamichat.api;

/**
 * The channel that is droppable
 */
public interface TemporaryChannel extends Channel {

    /**
     * Drop the channel, all the users who are chatting in this channel will be moved into global channel
     */
    void drop();

    /**
     * To check whether the channel is dropped
     * @return the status
     */
    boolean isDropped();

    /**
     * Make a user have access to the channel
     * @param user the user
     */
    void addUser(User user);

    /**
     * Make a user not have access to the channel
     * @param user the user
     */
    void removeUser(User user);

}
