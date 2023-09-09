package ru.practicum.exploreWithMe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.service.StatsServerService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsServerService statisticService;
    private static final String CONTROLLER_LOG = "Контроллер статистики получил запрос на {}{}";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveStatistic(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info(CONTROLLER_LOG, "сохранение элемента статистики: ", endpointHitDto);
        statisticService.saveStatistic(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStatistic(@RequestParam(value = "start") String startStr,
                                           @RequestParam(value = "end") String endStr,
                                           @RequestParam(required = false) String[] uris,
                                           @RequestParam(defaultValue = "false") boolean unique) {
        log.info(CONTROLLER_LOG, "получение элементов статистики с параметрами:",
                "\nstart " + startStr + "\nend " + endStr + "\nuris " + Arrays.toString(uris) + "\nunique" + unique);
        return statisticService.getStatistic(startStr, endStr, uris, unique);
    }
}
