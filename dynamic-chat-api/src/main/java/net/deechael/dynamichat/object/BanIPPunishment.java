package net.deechael.dynamichat.object;

import java.util.Date;

public interface BanIPPunishment {

    String getIP();

    String withUser();

    String getOperator();

    Date getStartDate();

    Date getEndDate();

    boolean hasUnbanned();

}
