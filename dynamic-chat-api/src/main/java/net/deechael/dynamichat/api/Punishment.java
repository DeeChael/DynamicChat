package net.deechael.dynamichat.api;

import java.util.Date;

public interface Punishment {

    String getId();

    String getUsername();

    String getOperator();

    Type getType();

    Date getStartDate();

    Date getEndDate();

    boolean hasUnbanned();

    String getReason();

    boolean isForever();

    enum Type {
        BAN, MUTE
    }

}
