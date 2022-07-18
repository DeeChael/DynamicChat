package net.deechael.dynamichat.object;

import java.util.Date;

/**
 * Example: 1y2mo3wk4d5h6min7s
 */
public interface Time {

    int getYears();

    int getMonths();

    int getWeeks();

    int getDays();

    int getHours();

    int getMinutes();

    int getSeconds();

    long getTotalSeconds();

    Date after(Date start);

}
