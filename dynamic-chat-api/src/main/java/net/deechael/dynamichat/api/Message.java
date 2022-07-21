package net.deechael.dynamichat.api;

/**
 * Message object that can get the user
 */
public interface Message extends MuteMessage {

    /**
     * The user sent the message
     * @return sender
     */
    User getSender();

    /**
     * To get the name of the sender
     * @return sender name
     */
    @Override
    default String getSenderName() {
        return getSender().getName();
    }

}
