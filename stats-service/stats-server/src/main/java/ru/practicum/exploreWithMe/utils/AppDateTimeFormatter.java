package ru.practicum.exploreWithMe.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@UtilityClass
public class AppDateTimeFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime toDateTime(String localDateTimeStr) {
        return LocalDateTime.parse(localDateTimeStr, FORMATTER);
    }

    public String toString(LocalDateTime localDateTime) {
        return localDateTime.format(FORMATTER);
    }
}
