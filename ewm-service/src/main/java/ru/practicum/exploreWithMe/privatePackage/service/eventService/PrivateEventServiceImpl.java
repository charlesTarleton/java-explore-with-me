package ru.practicum.exploreWithMe.privatePackage.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.event.dto.*;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.repository.LocationRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.event.utils.UserAction;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventDateTimeException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventLimitException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventUpdateStateException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.UserIsNotEventInitiatorException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.*;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.RequestChangeNotPendingStatusException;
import ru.practicum.exploreWithMe.commonFiles.request.dto.*;
import ru.practicum.exploreWithMe.commonFiles.request.model.Request;
import ru.practicum.exploreWithMe.commonFiles.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestMapper;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PrivateEventServiceImpl implements PrivateEventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private static final byte REQUIREMENT_HOURS_COUNT = 2;
    private static final String SERVICE_LOG = "Частный сервис событий получил запрос на {}{}";

    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {
        log.info(SERVICE_LOG, "получение событий, добавленных пользователем с id: ", userId);
        checkUserIsExist(userId);
        return eventRepository.getUserEvents(userId, new ExploreWithMePageable(from, size, Sort.unsorted())).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto addEvent(Long userId, NewEventDto eventDto) {
        log.info(SERVICE_LOG, "добавление нового события: ", eventDto);
        User user = checkUserIsExist(userId);
        Category category = checkCategoryIsExist(eventDto.getCategory());
        checkEventDateTime(eventDto.getEventDate());
        eventDto.setLocation(checkLocationIsExist(eventDto.getLocation()));
        Event event = EventMapper.toEvent(null, category, eventDto, 0, user, 0);
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        log.info(SERVICE_LOG, "получение добавленной пользователем информации о событии с id: ", eventId);
        checkUserIsExist(userId);
        Event event = checkEventIsExist(eventId);
        checkUserIsEventInitiator(userId, event.getInitiator().getId());
        return EventMapper.toFullDto(event);
    }

    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        log.info(SERVICE_LOG, "изменение добавленного пользователем события с id: ", eventId);
        User user = checkUserIsExist(userId);
        Category category = checkCategoryIsExist(eventDto.getCategory());
        Event oldEvent = checkEventIsExist(eventId);
        checkUserIsEventInitiator(userId, oldEvent.getInitiator().getId());
        checkEventDateTime(eventDto.getEventDate());
        checkEventStatus(oldEvent.getStateAction());
        if (eventDto.getLocation() != null &&
                eventDto.getLocation().getLat() != null &&
                eventDto.getLocation().getLon() != null) {
            eventDto.setLocation(checkLocationIsExist(eventDto.getLocation()));
        } else {
            eventDto.setLocation(oldEvent.getLocation());
        }
        Event event;
        if (eventDto.getStateAction().equals(UserAction.CANCEL_REVIEW)) {
            event = EventMapper.toEvent(eventId, category, eventDto, user, EventState.CANCELED, oldEvent);
        } else if (eventDto.getStateAction().equals(UserAction.SEND_TO_REVIEW)) {
            event = EventMapper.toEvent(eventId, category, eventDto, user, EventState.PENDING, oldEvent);
        } else {
            event = EventMapper.toEvent(eventId, category, eventDto, user, oldEvent.getStateAction(), oldEvent);
        }
        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        log.info(SERVICE_LOG, "получение информации о запросах на участие в событии с id: ", eventId);
        checkUserIsExist(userId);
        Event event = checkEventIsExist(eventId);
        checkUserIsEventInitiator(userId, event.getInitiator().getId());
        return requestRepository.getEventRequests(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult approvingEventRequest(Long userId, Long eventId,
                                                                EventRequestStatusUpdateRequest requestStatuses) {
        log.info(SERVICE_LOG, "изменение статуса заявок на участие в событии с id: ", eventId);
        checkUserIsExist(userId);
        Event event = checkEventIsExist(eventId);
        checkEventIsLimited(event);
        List<Request> requestList = requestRepository.findAllByRequestIdList(requestStatuses.getRequestIds());
        if (event.getParticipantLimit() != 0 && !event.getRequestModeration()) {
            for (Request request : requestList) {
                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    throw new RequestChangeNotPendingStatusException("Ошибка. Попытка изменить статус запроса на " +
                            "участие в событии, не находящееся в ожидании");
                }
                if (requestStatuses.getStatus().equals(RequestStatus.CONFIRMED)) {
                    if (event.getParticipantLimit() > event.getConfirmedRequests()) {
                        request.setStatus(RequestStatus.CONFIRMED);
                        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    } else {
                        request.setStatus(RequestStatus.REJECTED);
                    }
                } else {
                    request.setStatus(requestStatuses.getStatus());
                }
            }
            eventRepository.save(event);
            requestRepository.saveAll(requestList);
        }
        List<ParticipationRequestDto> requestDtoList = requestRepository.getEventRequests(eventId).stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
        return new EventRequestStatusUpdateResult(
                requestDtoList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
                        .collect(Collectors.toList()),
                requestDtoList.stream()
                        .filter(request -> request.getStatus().equals(RequestStatus.REJECTED))
                        .collect(Collectors.toList()));
    }

    private User checkUserIsExist(Long userId) {
        log.info("Начата процедура проверки наличия пользователя с id: {}", userId);
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserExistException("Указанный пользователь не найден");
        }
        return user.get();
    }

    private Category checkCategoryIsExist(Long categoryId) {
        log.info("Начата процедура проверки наличия категории с id: {}", categoryId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new CategoryExistException("Указанная категория не найдена");
        }
        return category.get();
    }

    private Event checkEventIsExist(Long eventId) {
        log.info("Начата процедура проверки наличия события с id: {}", eventId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new EventExistException("Указанное событие не найдено");
        }
        return event.get();
    }

    private void checkUserIsEventInitiator(Long userId, Long initiatorId) {
        log.info("Начата процедура проверки соответствия пользователя инициатору события с id: {}", initiatorId);
        if (userId.longValue() != initiatorId.longValue()) {
            throw new UserIsNotEventInitiatorException("Пользователь не является инициатором события");
        }
    }

    private void checkEventDateTime(LocalDateTime eventDateTime) {
        log.info("Начата процедура проверки соответствия даты и времени начала события требованиям приложения");
        if (eventDateTime.isAfter(LocalDateTime.now().plusHours(REQUIREMENT_HOURS_COUNT))) {
            throw new EventDateTimeException("До начала события осталось менее " + REQUIREMENT_HOURS_COUNT + " часов");
        }
    }

    private void checkEventStatus(EventState eventState) {
        log.info("Начата процедура проверки соответствия статуса события требованиям приложения");
        if (eventState != EventState.CANCELED || eventState != EventState.PENDING) {
            throw new EventUpdateStateException("Редактирование опубликованных событий не допускается");
        }
    }

    private void checkEventIsLimited(Event event) {
        if ((event.getParticipantLimit() == event.getConfirmedRequests().longValue()) &&
                event.getRequestModeration()) {
            throw new EventLimitException("Превышен лимит для направления запросов");
        }
    }

    private Location checkLocationIsExist(Location location) {
        log.info("Начата процедура проверки наличия локации и её сохранения(при отсутствии в базе данных)");
        Optional<Location> locationOptional = locationRepository.findById(location.getLat(), location.getLon());
        if (locationOptional.isEmpty()) {
            location = locationRepository.save(location);
        } else {
            location = locationOptional.get();
        }
        return location;
    }
}
