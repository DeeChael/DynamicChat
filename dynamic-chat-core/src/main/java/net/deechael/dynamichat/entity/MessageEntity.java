package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Message;
import net.deechael.dynamichat.api.User;

public class MessageEntity implements Message {

    private final User user;
    private final String content;

    private final String id;

    public MessageEntity(User user, String content, String id) {
        this.user = user;
        this.content = content.replaceAll("§x(§([a-fA-F\\d])){6}", "").replaceAll("§[abcdefklmnorABCDEFKLMNOR0123456789]", "");
        this.id = id;
    }

    @Override
    public User getSender() {
        return user;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getId() {
        return id;
    }
}
