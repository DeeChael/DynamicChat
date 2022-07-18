package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.exception.TimeParseException;
import net.deechael.dynamichat.object.Time;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeEntity implements Time {

    private static final long YEAR = 365 * 24 * 60 * 60;
    private static final long MONTH = 30 * 24 * 60 * 60;
    private static final long WEEK = 7 * 24 * 60 * 60;
    private static final long DAY = 24 * 60 * 60;
    private static final long HOUR = 60 * 60;
    private static final long MINUTE = 60;
    private static final long SECOND = 1;

    private final int years;
    private final int months;
    private final int weeks;
    private final int days;
    private final int hours;
    private final int minutes;
    private final int seconds;

    public TimeEntity(long totalSeconds) {
        int years = (int) (totalSeconds / YEAR);
        totalSeconds -= years * YEAR;
        int months = (int) (totalSeconds / MONTH);
        totalSeconds -= months * MONTH;
        int weeks = (int) (totalSeconds / WEEK);
        totalSeconds -= weeks * WEEK;
        int days = (int) (totalSeconds / DAY);
        totalSeconds -= days * DAY;
        int hours = (int) (totalSeconds / HOUR);
        totalSeconds -= hours * HOUR;
        int minutes = (int) (totalSeconds / MINUTE);
        totalSeconds -= minutes * MINUTE;
        int seconds = (int) totalSeconds;
        this.years = years;
        this.months = months;
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public TimeEntity(int years, int months, int weeks, int days, int hours, int minutes, int seconds) {
        this.years = years;
        this.months = months;
        this.weeks = weeks;
        this.days = days;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public static Time of(String timeString) {
        return new TimeEntity(analyze(timeString));
    }

    private static long analyze(String timeString) {
        long totalTime = 0;
        boolean foundYear = false;
        boolean foundMonth = false;
        boolean foundWeek = false;
        boolean foundDay = false;
        boolean foundHour = false;
        boolean foundMinute = false;
        boolean foundSecond = false;
        Matcher matcher = Pattern.compile("(\\d)*(y|mo|wk|d|h|min|s)").matcher(timeString);
        while (matcher.find()) {
            String timeObject = matcher.group();
            if (timeObject.endsWith("s")) {
                if (foundSecond)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 1)) * SECOND;
                foundSecond = true;
            } else if (timeObject.endsWith("min")) {
                if (foundMinute)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 3)) * MINUTE;
                foundMinute = true;
            } else if (timeObject.endsWith("h")) {
                if (foundHour)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 1)) * HOUR;
                foundHour = true;
            } else if (timeObject.endsWith("d")) {
                if (foundDay)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 1)) * DAY;
                foundDay = true;
            } else if (timeObject.endsWith("wk")) {
                if (foundWeek)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 2)) * WEEK;
                foundWeek = true;
            } else if (timeObject.endsWith("mo")) {
                if (foundMonth)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 2)) * MONTH;
                foundMonth = true;
            } else if (timeObject.endsWith("y")) {
                if (foundYear)
                    throw new TimeParseException("Failed to parse time");
                totalTime += Integer.parseInt(timeObject.substring(0, timeObject.length() - 1)) * YEAR;
                foundYear = true;
            } else {
                throw new TimeParseException("Failed to parse time");
            }
        }
        return totalTime;
    }

    @Override
    public int getYears() {
        return years;
    }

    @Override
    public int getMonths() {
        return months;
    }

    @Override
    public int getWeeks() {
        return weeks;
    }

    @Override
    public int getDays() {
        return days;
    }

    @Override
    public int getHours() {
        return hours;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public int getSeconds() {
        return seconds;
    }

    @Override
    public long getTotalSeconds() {
        return years * YEAR + months * MONTH + weeks * WEEK + days * DAY + hours * HOUR + minutes * MINUTE + seconds * SECOND;
    }

    @Override
    public Date after(Date start) {
        return new Date(start.getTime() + (getTotalSeconds() * 1000L));
    }

}
