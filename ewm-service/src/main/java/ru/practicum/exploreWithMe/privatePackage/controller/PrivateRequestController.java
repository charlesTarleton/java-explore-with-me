package ru.practicum.exploreWithMe.privatePackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.privatePackage.service.requestService.PrivateRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final PrivateRequestService privateService;
    private static final String CONTROLLER_LOG = "Частный контроллер запросов получил запрос на {}{}";

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable("userId") Long userId) {
        log.info(CONTROLLER_LOG, "получение всех запросов на участие в событиях от пользователя с id: ", userId);
        return privateService.getUserRequests(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable("userId") Long userId,
                                              @RequestParam("eventId") Long eventId) {
        log.info(CONTROLLER_LOG, "добавление запроса на участие в событии с id: ", eventId);
        return privateService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info(CONTROLLER_LOG, "отмену запроса с id: ", requestId);
        return privateService.cancelRequest(userId, requestId);
    }
}
