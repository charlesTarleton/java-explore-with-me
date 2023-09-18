package ru.practicum.exploreWithMe.publicPackage.service.eventService;

import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventSort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    List<EventShortDto> getEventsWithFiltration(String text, Long[] categories, Boolean paid, String rangeStartStr,
                                                String rangeEndStr, Boolean onlyAvailable, EventSort sort,
                                                Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);
}
