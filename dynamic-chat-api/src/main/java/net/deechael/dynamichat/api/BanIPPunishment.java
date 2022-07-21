package net.deechael.dynamichat.api;

import java.util.Date;

public interface BanIPPunishment {

    String getId();

    String getIP();

    String withUser();

    String getOperator();

    Date getStartDate();

    Date getEndDate();

    String getReason();

    boolean hasUnbanned();

    boolean isForever();

}
