package ru.practicum.exploreWithMe.service;

import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;

import java.util.List;

public interface StatsServerService {
    void saveStatistic(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStatistic(String startStr, String endStr, String[] uris, boolean unique);
}
