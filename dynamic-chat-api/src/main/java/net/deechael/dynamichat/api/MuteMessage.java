package net.deechael.dynamichat.api;

import java.util.Date;

/**
 * The object contains the name of the sender, content, id and the time when sent the message
 */
public interface MuteMessage {

    /**
     * Get the name of the sender
     *
     * @return sender name
     */
    String getSenderName();

    /**
     * Get the content of the messsage
     *
     * @return content
     */
    String getContent();

    /**
     * Get the id of the message, made up with 64 characters
     *
     * @return the id
     */
    String getId();

    /**
     * Get the time when the message was sent
     *
     * @return send time
     */
    Date getSendTime();

}
