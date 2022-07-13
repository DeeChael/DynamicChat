package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.User;

public class MessageEntity implements Message {

    private final User user;
    private final String content;

    public MessageEntity(User user, String content) {
        this.user = user;
        this.content = content.replaceAll("§x(§([a-fA-F\\d])){6}", "").replaceAll("§[abcdefklmnorABCDEFKLMNOR0123456789]", "");
    }

    @Override
    public User getSender() {
        return user;
    }

    @Override
    public String getContent() {
        return content;
    }

}
