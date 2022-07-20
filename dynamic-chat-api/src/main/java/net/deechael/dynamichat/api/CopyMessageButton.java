package net.deechael.dynamichat.api;

public interface CopyMessageButton extends MessageButton {

    String value(User clicker, Message message);

    @Override
    default void click(User clicker, Message message) {
    }

}
