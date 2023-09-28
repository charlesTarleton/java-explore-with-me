package ru.practicum.exploreWithMe.publicPackage.service.eventService;

import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEventsWithFiltration(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort,
                                                Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);
}
