package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.User;

public class MessageEntity extends MuteMessageEntity implements Message {

    private final User user;
    public MessageEntity(User user, String content, String id) {
        super(user.getName(), content, id);
        this.user = user;
    }

    @Override
    public User getSender() {
        return user;
    }

}
