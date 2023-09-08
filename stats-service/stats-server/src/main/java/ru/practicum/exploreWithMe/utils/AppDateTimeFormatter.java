package ru.practicum.exploreWithMe.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@UtilityClass
public class AppDateTimeFormatter {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public LocalDateTime toDateTime(String localDateTimeStr) {
        return LocalDateTime.parse(localDateTimeStr, formatter);
    }

    public String toString(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }
}
