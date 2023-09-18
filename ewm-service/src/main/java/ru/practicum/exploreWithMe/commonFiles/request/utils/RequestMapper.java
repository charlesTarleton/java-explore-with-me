package ru.practicum.exploreWithMe.commonFiles.request.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.commonFiles.request.model.Request;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
@Slf4j
public class RequestMapper {
    public ParticipationRequestDto toDto(Request request) {
        log.info("Начата процедура преобразования запроса на участие в событии в ДТО");
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public Request toRequest(User user, Event event, RequestStatus status) {
        log.info("Начата процедура преобразования ДТО в запрос на участие в событии");
        return new Request(null, LocalDateTime.now(), event, user, status);
    }
}
