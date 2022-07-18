package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.MuteMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MuteMessageEntity implements MuteMessage {

    private final String sender;
    private final String content;
    private final String id;

    private final Date sendTime;

    public MuteMessageEntity(String sender, String content, String id, String sendTime) {
        this.sender = sender;
        this.content = content.replaceAll("§x(§([a-fA-F\\d])){6}", "").replaceAll("§[abcdefklmnorABCDEFKLMNOR0123456789]", "");
        this.id = id;
        try {
            this.sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sendTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getId() {
        return id;
    }

    @Override
    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getSenderName() {
        return sender;
    }

}
