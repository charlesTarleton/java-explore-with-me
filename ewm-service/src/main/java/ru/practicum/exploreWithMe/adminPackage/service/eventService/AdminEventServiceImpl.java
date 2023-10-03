package ru.practicum.exploreWithMe.adminPackage.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.utils.CommentMapper;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.LocationDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.repository.LocationRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.AdminAction;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.event.utils.LocationMapper;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundred.EventDateTimePastException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventDateTimeException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventUpdateStateException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CategoryExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.EVENT_SERVICE_LOG;
import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.REQUIREMENT_HOURS_COUNT;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithSettings(Long[] users, String[] states, Long[] categories,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                                    Integer size) {
        log.info(EVENT_SERVICE_LOG, "получение списка событий по параметрам", "");
        Set<Long> usersSet = null;
        if (users != null) {
            usersSet = Set.of(users);
        }
        Set<EventState> statesSet = null;
        if (states != null) {
            statesSet = Arrays.stream(states)
                    .map(EventState::valueOf)
                    .collect(Collectors.toSet());
        }
        Set<Long> categoriesSet = null;
        if (categories != null) {
            categoriesSet = Set.of(categories);
        }
        checkSearchDateTime(rangeStart, rangeEnd);
        List<Event> eventList = eventRepository.getEventsWithSettings(usersSet, statesSet, categoriesSet,
                new ExploreWithMePageable(from, size, Sort.unsorted()));
        List<Long> eventIdList = eventList.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Long, List<CommentDto>> commentMap = commentRepository.getEventsComments(eventIdList).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getEventId));
        Stream<Event> eventStream;
        if (rangeStart != null && rangeEnd != null) {
            eventStream = eventList.stream().filter(event -> event.getEventDate().isAfter(rangeStart)
                    && event.getEventDate().isBefore(rangeEnd));
        } else if (rangeStart != null) {
            eventStream = eventList.stream().filter(event -> event.getEventDate().isAfter(rangeStart));
        } else if (rangeEnd != null) {
            eventStream = eventList.stream().filter(event -> event.getEventDate().isBefore(rangeEnd));
        } else {
            eventStream = eventList.stream();
        }
        return eventStream
                .map(event -> EventMapper.toFullDto(event, commentMap.get(event.getId())))
                .collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto) {
        log.info(EVENT_SERVICE_LOG, "изменение события с id: ", eventId);
        Category category;
        Event oldEvent = checkEventIsExist(eventId);
        Location location;
        if (eventDto.getLocation() != null &&
                eventDto.getLocation().getLat() != null &&
                eventDto.getLocation().getLon() != null) {
            location = checkLocationIsExist(eventDto.getLocation());
        } else {
            location = oldEvent.getLocation();
        }
        if (eventDto.getCategory() != null) {
            category = checkCategoryIsExist(eventDto.getCategory());
        } else {
            category = oldEvent.getCategory();
        }
        Event event;
        if (eventDto.getStateAction() != null) {
            checkEventStatus(oldEvent.getStateAction(), eventDto.getStateAction());
        }
        if (eventDto.getEventDate() != null) {
            checkEventDateTime(eventDto.getEventDate());
        }
        if (eventDto.getStateAction() != null && eventDto.getStateAction().equals(AdminAction.PUBLISH_EVENT)) {
            if (eventDto.getEventDate() != null) {
                checkEventDateTimeForPublish(eventDto.getEventDate());
            } else {
                checkEventDateTimeForPublish(oldEvent.getEventDate());
            }
            event = EventMapper.toEvent(eventId, category, eventDto, oldEvent, location, EventState.PUBLISHED);
        } else {
            event = EventMapper.toEvent(eventId, category, eventDto, oldEvent, location, EventState.REJECTED);
        }
        List<CommentDto> commentDtoList = commentRepository.getEventComments(eventId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        return EventMapper.toFullDto(eventRepository.save(event), commentDtoList);
    }

    private Category checkCategoryIsExist(Long categoryId) {
        log.info("Начата процедура проверки наличия категории с id: {}", categoryId);
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryExistException("Указанная категория не найдена"));
    }

    private Event checkEventIsExist(Long eventId) {
        log.info("Начата процедура проверки наличия события с id: {}", eventId);
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventExistException("Указанное событие не найдено"));
    }

    private void checkEventDateTimeForPublish(LocalDateTime eventDateTime) {
        log.info("Начата процедура проверки даты и времени начала события до наступления события");
        if (eventDateTime.isBefore(LocalDateTime.now().plusHours(REQUIREMENT_HOURS_COUNT))) {
            throw new EventDateTimeException("До начала события осталось менее " + REQUIREMENT_HOURS_COUNT + " часов");
        }
    }

    private void checkEventDateTime(LocalDateTime eventDateTime) {
        log.info("Начата процедура проверки даты и времени начала события на указание будущего события");
        if (eventDateTime.isBefore(LocalDateTime.now())) {
            throw new EventDateTimePastException("Дата события не может быть в прошлом");
        }
    }

    private void checkSearchDateTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        log.info("Начата процедура проверки на противоречия между датой начала и датой окончания поиска");
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventDateTimeException("Начало диапазона не может быть позднее его окончания");
        }
    }

    private void checkEventStatus(EventState eventState, AdminAction adminAction) {
        log.info("Начата процедура проверки соответствия статуса события требованиям приложения");
        if (!eventState.equals(EventState.PENDING) && adminAction.equals(AdminAction.PUBLISH_EVENT)) {
            throw new EventUpdateStateException("Редактирование события на публикацию администратором допускается " +
                    "только при статусе ожидания");
        }
        if (eventState.equals(EventState.PUBLISHED) && adminAction.equals(AdminAction.REJECT_EVENT)) {
            throw new EventUpdateStateException("Нельзя отказать в публикации ранее опубликованного события");
        }
    }

    private Location checkLocationIsExist(LocationDto location) {
        log.info("Начата процедура проверки наличия локации и её сохранения(при отсутствии в базе данных)");
        Optional<Location> locationOptional = locationRepository
                .findByCoordinates(location.getLat(), location.getLon());
        return locationOptional.orElseGet(() -> locationRepository.save(LocationMapper.toLocation(location)));
    }
}
