package net.deechael.dynamichat.api;

import java.util.Date;

/**
 * y: Year
 * mo: Month
 * wk: Week
 * d: Day
 * min: Minute
 * s: Second
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
