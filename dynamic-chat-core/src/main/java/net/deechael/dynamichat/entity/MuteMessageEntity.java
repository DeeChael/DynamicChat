package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.MuteMessage;

public class MuteMessageEntity implements MuteMessage {

    private final String sender;
    private final String content;
    private final String id;

    public MuteMessageEntity(String sender, String content, String id) {
        this.sender = sender;
        this.content = content.replaceAll("§x(§([a-fA-F\\d])){6}", "").replaceAll("§[abcdefklmnorABCDEFKLMNOR0123456789]", "");
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getSenderName() {
        return sender;
    }

}
