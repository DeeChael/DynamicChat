package net.deechael.dynamichat.api;

/**
 * Button to copy message
 */
public interface CopyMessageButton extends MessageButton {

    /**
     * To process the value the user will copy
     *
     * @param clicker the user
     * @param message the message sent by the user
     * @return the value the user will copy
     */
    String value(User clicker, Message message);

    /**
     * Not work with CopyMessageButton
     */
    @Override
    default void click(User clicker, Message message) {
    }

}
