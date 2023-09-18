package ru.practicum.exploreWithMe.adminPackage.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.UpdateEventAdminRequest;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.AdminAction;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventDateTimeException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventUpdateStateException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CategoryExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.AppDateTimeFormatter;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private static final String SERVICE_LOG = "Сервис событий администратора получил запрос на {}{}";
    private static final byte REQUIREMENT_HOURS_COUNT = 1;

    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsWithSettings(Long[] users, String[] states, Long[] categories,
                                                    String rangeStartStr, String rangeEndStr, Integer from,
                                                    Integer size) {
        log.info(SERVICE_LOG, "получение списка событий по параметрам", "");
        LocalDateTime rangeStart = AppDateTimeFormatter.toDateTime(rangeStartStr);
        LocalDateTime rangeEnd = AppDateTimeFormatter.toDateTime(rangeEndStr);
        checkSearchDateTime(rangeStart, rangeEnd);
        return eventRepository.getEventsWithSettings(users, states, categories, rangeStart, rangeEnd,
                        new ExploreWithMePageable(from, size, Sort.unsorted())).stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
    }

    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest eventDto) {
        log.info(SERVICE_LOG, "изменение события с id: ", eventId);
        Category category = checkCategoryIsExist(eventDto.getCategory());
        Event oldEvent = checkEventIsExist(eventId);
        checkEventDateTime(eventDto.getEventDate());
        checkEventStatus(oldEvent.getStateAction(), eventDto.getStateAction());
        Event event;
        if (eventDto.getStateAction().equals(AdminAction.PUBLISH_EVENT)) {
            event = EventMapper.toEvent(eventId, category, eventDto, oldEvent, EventState.PUBLISHED);
        } else {
            event = EventMapper.toEvent(eventId, category, eventDto, oldEvent, EventState.REJECTED);
        }
        return EventMapper.toFullDto(eventRepository.save(event));
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

    private void checkEventDateTime(LocalDateTime eventDateTime) {
        log.info("Начата процедура проверки соответствия даты и времени начала события требованиям приложения");
        if (eventDateTime.isAfter(LocalDateTime.now().plusHours(REQUIREMENT_HOURS_COUNT))) {
            throw new EventDateTimeException("До начала события осталось менее " + REQUIREMENT_HOURS_COUNT + " часов");
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
}
