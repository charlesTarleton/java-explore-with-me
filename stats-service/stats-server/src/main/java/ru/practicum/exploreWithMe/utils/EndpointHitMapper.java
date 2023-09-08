package ru.practicum.exploreWithMe.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.model.EndpointHit;

@UtilityClass
@Slf4j
public class EndpointHitMapper {
    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        log.info("Начата процедура преобразования ДТО в элемент статистики: {}", endpointHitDto);
        return new EndpointHit(
                null,
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp());
    }
}
