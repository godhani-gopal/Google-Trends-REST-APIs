package com.googleTrendsBigQuery.googleTrendsRestApis.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Parses the week string and ensures the date is not in the future
    public static LocalDate parseWeek(String week) {
        if (week == null || week.isEmpty()) {
            return null;
        }

        try {
            LocalDate parsedDate = LocalDate.parse(week, FORMATTER);
            LocalDate today = LocalDate.now();

            if (parsedDate.isAfter(today)) {
                throw new IllegalArgumentException("The date cannot be in the future.");
            }

            return parsedDate;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}