package net.deechael.dynamichat.extension.report;

import java.util.Date;

public class DateUtils {

    public static long delay(Date date1, Date date2) {
        return Math.abs(date1.getTime() - date2.getTime());
    }

}
