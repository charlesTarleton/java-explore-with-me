package ru.practicum.exploreWithMe.commonFiles.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
@Slf4j
public class AppDateTimeFormatter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTime toDateTime(String localDateTimeStr) {
        log.info("Начата процедура преобразования строки в дату: {}", localDateTimeStr);
        try {
            return LocalDateTime.parse(localDateTimeStr, FORMATTER);
        } catch (DateTimeParseException | NullPointerException e) {
            return null;
        }
    }

    public String toString(LocalDateTime localDateTime) {
        log.info("Начата процедура преобразования даты в строку: {}", localDateTime);
        return localDateTime.format(FORMATTER);
    }
}
