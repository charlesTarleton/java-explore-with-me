package ru.practicum.exploreWithMe.privatePackage.service.requestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.RequestRepeatException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.UserIsNotEventInitiatorException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.UserIsNotRequesterException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.RequestExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.UserExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventLimitException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventNotPublicisedException;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.commonFiles.request.model.Request;
import ru.practicum.exploreWithMe.commonFiles.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestMapper;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private static final String SERVICE_LOG = "Частный сервис запросов получил запрос на {}{}";

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info(SERVICE_LOG, "получение всех запросов на участие в событиях от пользователя с id: ", userId);
        checkUserIsExist(userId);
        return requestRepository.getUserRequests(userId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info(SERVICE_LOG, "добавление запроса на участие в событии с id: ", eventId);
        User user = checkUserIsExist(userId);
        Event event = checkEventIsExist(eventId);
        checkUserIsEventInitiator(userId, event.getInitiator().getId());
        checkIsUserRepeatRequest(userId, eventId);
        checkEventIsPublished(event);
        Request request;
        if (event.getParticipantLimit() == 0) {
            request = handleIfNotRequireModeration(user, event);
        } else {
            checkEventIsLimited(event);
            if (event.getRequestModeration()) {
                request = RequestMapper.toRequest(user, event, RequestStatus.PENDING);
            } else {
                request = handleIfNotRequireModeration(user, event);
            }
        }
        return RequestMapper.toDto(requestRepository.save(request));
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info(SERVICE_LOG, "отмену запроса с id: ", requestId);
        checkUserIsExist(userId);
        Request request = checkRequestIsExist(requestId);
        checkUserIsRequester(userId, request.getRequester().getId());
        if (request.getStatus().equals(RequestStatus.CONFIRMED) &&
                request.getEvent().getRequestModeration() && request.getEvent().getParticipantLimit() != 0) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() - 1);
            eventRepository.save(event);
        }
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toDto(requestRepository.save(request));
    }

    private Request handleIfNotRequireModeration(User user, Event event) {
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        return RequestMapper.toRequest(user, event, RequestStatus.CONFIRMED);
    }

    private User checkUserIsExist(Long userId) {
        log.info("Начата процедура проверки наличия пользователя с id: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserExistException("Указанный пользователь не найден");
        }
        return user.get();
    }

    private Request checkRequestIsExist(Long requestId) {
        log.info("Начата процедура проверки наличия запроса на участие в событии с id: {}", requestId);
        Optional<Request> request = requestRepository.findById(requestId);
        if (request.isEmpty()) {
            throw new RequestExistException("Указанный запрос на участие в событии не найден");
        }
        return request.get();
    }

    private Event checkEventIsExist(Long eventId) {
        log.info("Начата процедура проверки наличия события с id: {}", eventId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EventExistException("Указанное событие не найдено");
        }
        return event.get();
    }

    private void checkEventIsPublished(Event event) {
        if (!event.getStateAction().equals(EventState.PUBLISHED)) {
            throw new EventNotPublicisedException("Указанное событие не опубликовано");
        }
    }

    private void checkEventIsLimited(Event event) {
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new EventLimitException("Превышен лимит для направления запросов");
        }
    }

    private void checkUserIsEventInitiator(Long userId, Long initiatorId) {
        log.info("Начата процедура проверки несоответствия пользователя инициатору события с id: {}", initiatorId);
        if (userId.longValue() == initiatorId.longValue()) {
            throw new UserIsNotEventInitiatorException("Пользователь является инициатором события");
        }
    }

    private void checkUserIsRequester(Long userId, Long requesterId) {
        log.info("Начата процедура проверки соответствия пользователя просителю с id: {}", requesterId);
        if (userId.longValue() != requesterId.longValue()) {
            throw new UserIsNotRequesterException("Пользователь не является просителю");
        }
    }

    private void checkIsUserRepeatRequest(Long userId, Long eventId) {
        log.info("Начата процедура проверки на повторный запрос от пользователя");
        Optional<Request> request = requestRepository.getUserEventRequest(eventId, userId);
        if (request.isPresent()) {
            throw new RequestRepeatException("Пользователь направил повторный запрос");
        }
    }
}
