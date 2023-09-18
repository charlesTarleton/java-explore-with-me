package ru.practicum.exploreWithMe.adminPackage.service.eventService;

import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getEventsWithSettings(Long[] users, String[] states, Long[] categories,
                                             String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto);
}
