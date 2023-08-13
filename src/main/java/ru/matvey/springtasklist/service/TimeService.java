package ru.matvey.springtasklist.service;

import java.time.LocalDateTime;

public class TimeService {

    public static String getTimeStamp(LocalDateTime time) {
        return String.format("[%02d.%02d.%02d %02d:%02d:%02d]",
                time.getYear(),
                time.getMonthValue(),
                time.getDayOfWeek(),
                time.getHour(),
                time.getMinute(),
                time.getSecond());
    }

    public String convertMillisToString(long millis) {
        long seconds = millis / 1000 % 60;
        long minutes = millis / (60 * 1000) % 60;
        long hours = millis / (60 * 60 * 1000) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
