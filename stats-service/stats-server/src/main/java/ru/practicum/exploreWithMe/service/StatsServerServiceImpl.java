package ru.practicum.exploreWithMe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.dto.*;
import ru.practicum.exploreWithMe.exception.DateTimeValidationException;
import ru.practicum.exploreWithMe.model.ViewStats;
import ru.practicum.exploreWithMe.repository.StatsServerRepository;
import ru.practicum.exploreWithMe.utils.EndpointHitMapper;
import ru.practicum.exploreWithMe.utils.ViewStatsMapper;

import java.time.LocalDateTime;
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
    public List<ViewStatsDto> getStatistic(LocalDateTime start, LocalDateTime end,
                                           String[] incorrectUris, boolean unique) {
        log.info(SERVICE_LOG, "получение элементов статистики с параметрами:", "\nstart " + start +
                "\nend " + end + "\nuris " + Arrays.toString(incorrectUris) + "\nunique" + unique);
        String[] uris;
        if (incorrectUris != null && incorrectUris.length != 0 && incorrectUris[0].startsWith("[") &&
                incorrectUris[0].endsWith("]")) {
            uris = updateArray(incorrectUris);
        } else {
            uris = incorrectUris;
        }
        if (start.isAfter(end)) {
            throw new DateTimeValidationException("Дата начала не может быть позднее даты окончания");
        }

        boolean isArrayExist = uris != null && uris.length != 0;
        List<ViewStats> viewStatsList;
        if (isArrayExist && unique) {
            viewStatsList = statisticRepository.getStatisticIfUniqueAndWithArray(start, end, uris);

        } else if (!isArrayExist && unique) {
            viewStatsList = statisticRepository.getStatisticIfUniqueAndWithoutArray(start, end);
        } else if (!isArrayExist && !unique) {
            viewStatsList = statisticRepository.getStatisticIfNotUniqueAndWithoutArray(start, end);
        } else {
            viewStatsList = statisticRepository.getStatisticIfNotUniqueAndWithArray(start, end, uris);
        }
        return viewStatsList.stream().map(ViewStatsMapper::toDto).collect(Collectors.toList());
    }

    private String[] updateArray(String[] uris) {
        if (uris == null) {
            return null;
        }
        String[] correctUris = new String[uris.length];
        for (int i = 0; i < uris.length; i++) {
            correctUris[i] = uris[i].substring(1, uris[i].length() - 1);
        }
        return correctUris;
    }
}