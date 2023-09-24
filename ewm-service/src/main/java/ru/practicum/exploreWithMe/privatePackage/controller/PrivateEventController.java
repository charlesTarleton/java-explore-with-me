package ru.practicum.exploreWithMe.privatePackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.NewEventDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventUserRequest;
import ru.practicum.exploreWithMe.commonFiles.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.commonFiles.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.privatePackage.service.eventService.PrivateEventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateService;
    private static final String CONTROLLER_LOG = "Частный контроллер событий получил запрос на {}{}";

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable("userId") Long userId,
                                             @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                 Integer from,
                                             @Positive @RequestParam(value = "size", defaultValue = "10")
                                                 Integer size) {
        log.info(CONTROLLER_LOG, "получение событий, добавленных пользователем с id: ", userId);
        return privateService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") Long userId,
                                 @Valid @RequestBody NewEventDto eventDto) {
        log.info(CONTROLLER_LOG, "добавление нового события: ", eventDto);
        return privateService.addEvent(userId, eventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable("userId") Long userId,
                                     @PathVariable("eventId") Long eventId) {
        log.info(CONTROLLER_LOG, "получение добавленной пользователем информации о событии с id: ", eventId);
        return privateService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.info(CONTROLLER_LOG, "изменение добавленного пользователем события с id: ", eventId);
        return privateService.updateEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequests(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId) {
        log.info(CONTROLLER_LOG, "получение информации о запросах на участие в событии с id: ", eventId);
        return privateService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult approvingEventRequest(@PathVariable("userId") Long userId,
                                                                @PathVariable("eventId") Long eventId,
                                                                @Valid @RequestBody EventRequestStatusUpdateRequest
                                                                            requestStatuses) {
        log.info(CONTROLLER_LOG, "изменение статуса заявок на участие в событии с id: ", eventId);
        return privateService.approvingEventRequest(userId, eventId, requestStatuses);
    }
}
