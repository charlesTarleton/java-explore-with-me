package ru.practicum.exploreWithMe.privatePackage.service.eventService;

import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.NewEventDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventUserRequest;
import ru.practicum.exploreWithMe.commonFiles.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.commonFiles.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventService {
    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, NewEventDto eventDto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult approvingEventRequest(Long userId, Long eventId,
                                                         EventRequestStatusUpdateRequest requestStatuses);
}
