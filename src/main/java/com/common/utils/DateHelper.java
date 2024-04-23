package com.common.utils;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateHelper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DateHelper() {}

    public static Date getCurrentDate() {
        return new Date();
    }
    public static long getCurrentTime() {
        return getCurrentDate().getTime();
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
