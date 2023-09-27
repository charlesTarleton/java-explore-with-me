package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.eventService.AdminEventService;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.EVENT_CONTROLLER_LOG;
import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Common.PATTERN;

@RestController
@Slf4j
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class AdminEventController {
    private final AdminEventService adminService;

    @GetMapping
    public List<EventFullDto> getEventsWithSettings(@RequestParam(value = "users", required = false) Long[] users,
                                                    @RequestParam(value = "states", required = false) String[] states,
                                                    @RequestParam(value = "categories", required = false)
                                                        Long[] categories,
                                                    @RequestParam(value = "rangeStart", required = false)
                                                        @DateTimeFormat(pattern = PATTERN)
                                                        LocalDateTime rangeStart,
                                                    @RequestParam(value = "rangeEnd", required = false)
                                                        @DateTimeFormat(pattern = PATTERN)
                                                        LocalDateTime rangeEnd,
                                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                        Integer from,
                                                    @Positive @RequestParam(value = "size", defaultValue = "10")
                                                        Integer size) {
        log.info(EVENT_CONTROLLER_LOG, "получение списка событий по параметрам", "");
        return adminService.getEventsWithSettings(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info(EVENT_CONTROLLER_LOG, "изменение события с id: ", eventId);
        return adminService.updateEvent(eventId, eventDto);
    }
}
