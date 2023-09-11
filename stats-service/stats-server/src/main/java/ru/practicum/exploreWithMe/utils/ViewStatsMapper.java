package ru.practicum.exploreWithMe.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.model.ViewStats;

@UtilityClass
@Slf4j
public class ViewStatsMapper {
    public ViewStatsDto toDto(ViewStats viewStats) {
        log.info("Начата процедура преобразования публичного элемент статистики в ДТО: {}", viewStats);
        return new ViewStatsDto(
                viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHits().intValue());
    }

}
