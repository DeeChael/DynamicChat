package net.deechael.dynamichat.api;

import java.util.Date;

public interface MuteMessage {

    String getSenderName();

    String getContent();

    String getId();

    Date getSendTime();

}
