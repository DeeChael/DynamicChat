package net.deechael.dynamichat.api;

public interface Message extends MuteMessage {

    User getSender();

    @Override
    default String getSenderName() {
        return getSender().getName();
    }

}
