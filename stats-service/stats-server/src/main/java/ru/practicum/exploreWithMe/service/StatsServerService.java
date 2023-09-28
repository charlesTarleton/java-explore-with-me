package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerService {
    void saveStatistic(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
