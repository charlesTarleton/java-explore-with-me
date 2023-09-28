package ru.practicum.exploreWithMe.publicPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventSort;
import ru.practicum.exploreWithMe.publicPackage.service.eventService.PublicEventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Common.PATTERN;
import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.EVENT_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final PublicEventServiceImpl publicService;

    @GetMapping
    public List<EventShortDto> getEventsWithFiltration(@RequestParam(value = "text", required = false) String text,
                                                       @RequestParam(value = "categories", required = false)
                                                           Long[] categories,
                                                       @RequestParam(value = "paid", required = false) Boolean paid,
                                                       @RequestParam(value = "rangeStart", required = false)
                                                           @DateTimeFormat(pattern = PATTERN)
                                                           LocalDateTime rangeStart,
                                                       @RequestParam(value = "rangeEnd", required = false)
                                                           @DateTimeFormat(pattern = PATTERN)
                                                           LocalDateTime rangeEnd,
                                                       @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                                           Boolean onlyAvailable,
                                                       @RequestParam(value = "sort", required = false) EventSort sort,
                                                       @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                           Integer size,
                                                       HttpServletRequest request) {
        log.info(EVENT_CONTROLLER_LOG, "поиск событий по фильтру пользователя", "");
        return publicService.getEventsWithFiltration(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable("eventId") Long eventId, HttpServletRequest request) {
        log.info(EVENT_CONTROLLER_LOG, "получение расширенной информации о событии с id: ", eventId);
        return publicService.getEvent(eventId, request);
    }
}
