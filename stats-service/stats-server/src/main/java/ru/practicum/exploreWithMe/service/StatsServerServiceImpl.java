package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.exception.DateTimeValidationException;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.repository.StatsServerRepository;
import ru.practicum.exploreWithMe.utils.AppDateTimeFormatter;
import ru.practicum.exploreWithMe.utils.EndpointHitMapper;
import ru.practicum.exploreWithMe.utils.ViewStatsMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StatsServerServiceImpl implements StatsServerService {
    private final StatsServerRepository statisticRepository;
    private static final String SERVICE_LOG = "Сервис статистики получил запрос на {}{}";

    public void saveStatistic(EndpointHitDto endpointHitDto) {
        log.info(SERVICE_LOG, "сохранение элемента статистики: ", endpointHitDto);
        statisticRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
    }

    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStatistic(String startStr, String endStr, String[] uris, boolean unique) {
        log.info(SERVICE_LOG, "получение элементов статистики с параметрами:",
                "\nstart " + startStr + "\nend " + endStr + "\nuris " + Arrays.toString(uris) + "\nunique" + unique);

        LocalDateTime start = AppDateTimeFormatter.toDateTime(startStr);
        LocalDateTime end = AppDateTimeFormatter.toDateTime(endStr);
        if (start.isAfter(end)) {
            throw new DateTimeValidationException("Дата начала не может быть выше даты окончания");
        }

        boolean isArrayExist = uris != null && uris.length != 0;
        List<ViewStats> viewStatsList;
        if (isArrayExist && unique) {
            viewStatsList = new ArrayList<>();
            for (String uri : uris) {
                viewStatsList.addAll(statisticRepository.getStatisticIfUniqueAndWithArray(start, end, uri));
            }
        } else if (!isArrayExist && unique) {
            viewStatsList = statisticRepository.getStatisticIfUniqueAndWithoutArray(start, end);
        } else if (!isArrayExist && !unique) {
            viewStatsList = statisticRepository.getStatisticIfNotUniqueAndWithoutArray(start, end);
        } else {
            viewStatsList = new ArrayList<>();
            for (String uri : uris) {
                viewStatsList.addAll(statisticRepository.getStatisticIfNotUniqueAndWithArray(start, end, uri));
            } // К сожалению, в данном случае я не вижу способов избежать множественности запросов
        }//т.к. IN и нативные запросы PostgreSQL нельзя полностью или частично совместить с LOWER и CONCAT(string, '%')
        return viewStatsList.stream().map(ViewStatsMapper::toDto).collect(Collectors.toList()); // и ViewStats
    }
}