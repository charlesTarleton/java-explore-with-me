package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.eventService.AdminEventServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventServiceImpl adminService;
    private static final String CONTROLLER_LOG = "Контроллер событий администратора получил запрос на {}{}";

    @GetMapping
    public List<EventFullDto> getEventsWithSettings(@RequestParam(value = "users", required = false) Long[] users,
                                                    @RequestParam(value = "states", required = false) String[] states,
                                                    @RequestParam(value = "categories", required = false)
                                                        Long[] categories,
                                                    @RequestParam(value = "rangeStart", required = false)
                                                        String rangeStart,
                                                    @RequestParam(value = "rangeEnd", required = false)
                                                        String rangeEnd,
                                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(CONTROLLER_LOG, "получение списка событий по параметрам", "");
        return adminService.getEventsWithSettings(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest eventDto) {
        log.info(CONTROLLER_LOG, "изменение события с id: ", eventId);
        return adminService.updateEvent(eventId, eventDto);
    }
}
