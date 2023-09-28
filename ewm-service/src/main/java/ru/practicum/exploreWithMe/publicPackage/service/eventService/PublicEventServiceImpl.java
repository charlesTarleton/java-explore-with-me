package ru.practicum.exploreWithMe.publicPackage.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.client.StatsClient;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.utils.CommentMapper;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventSort;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundred.EventDateTimeRangeException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.EVENT_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final StatsClient client;
    private static final LocalDateTime INTERNET_BIRTHDAY = LocalDateTime
            .of(1969, 10, 29, 21, 0, 0);

    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsWithFiltration(String text, Long[] categories, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable, EventSort sort, Integer from,
                                                       Integer size, HttpServletRequest request) {
        log.info(EVENT_SERVICE_LOG, "поиск событий по фильтру пользователя", "");
        client.saveStatistic(new EndpointHitDto(request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now()));
        Pageable pageable = new ExploreWithMePageable(from, size, Sort.unsorted());
        if (sort != null) {
            switch (sort) {
                case EVENT_DATE:
                    pageable = new ExploreWithMePageable(from, size, Sort.by("eventDate").descending());
                    break;
                case VIEWS:
                    pageable = new ExploreWithMePageable(from, size, Sort.by("views").descending());
            }
        }
        List<Event> eventList = eventRepository.getEventsWithFiltration(text, Set.of(categories),
                paid, onlyAvailable, pageable);
        Map<Long, List<CommentDto>> commentMap = commentRepository.getEventsComments(eventList.stream()
                        .map(Event::getId)
                        .collect(Collectors.toList())).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getEventId));
        if (rangeStart == null || rangeEnd == null) {
            LocalDateTime currentTime = LocalDateTime.now();
            return eventList.stream()
                    .filter(event -> event.getEventDate().isAfter(currentTime))
                    .map(event -> EventMapper.toShortDto(event, commentMap.get(event.getId())))
                    .collect(Collectors.toList());
        } else {
            checkSearchDateTime(rangeStart, rangeEnd);
            return eventList.stream()
                    .filter(event -> event.getEventDate().isAfter(rangeStart) &&
                            event.getEventDate().isBefore(rangeEnd))
                    .map(event -> EventMapper.toShortDto(event, commentMap.get(event.getId())))
                    .collect(Collectors.toList());
        }
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        log.info(EVENT_SERVICE_LOG, "получение расширенной информации о событии с id: ", eventId);
        client.saveStatistic(new EndpointHitDto(request.getRequestURI(),
                request.getRemoteAddr(), LocalDateTime.now()));
        int views = checkViewsCount(request);
        Event event = checkEventIsExist(eventId);
        event.setViews(views);
        eventRepository.save(event);
        List<CommentDto> commentDtoList = commentRepository.getEventComments(eventId).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        return EventMapper.toFullDto(event, commentDtoList);
    }

    private void checkSearchDateTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        log.info("Начата процедура проверки на противоречия между датой начала {} и датой окончания поиска {}",
                rangeStart, rangeEnd);
        if (rangeStart.isAfter(rangeEnd)) {
            throw new EventDateTimeRangeException("Начало диапазона не может быть позднее его окончания");
        }
    }

    private Event checkEventIsExist(Long eventId) {
        log.info("Начата процедура проверки наличия события с id: {}", eventId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty() || !event.get().getStateAction().equals(EventState.PUBLISHED)) {
            throw new EventExistException("Указанное событие не найдено");
        }
        return event.get();
    }

    private int checkViewsCount(HttpServletRequest request) {
        log.info("Начата процедура проверки количества уникальных просмотров события");
        Object object = client.getStatistic(INTERNET_BIRTHDAY, LocalDateTime.now(),
                new String[]{request.getRequestURI()}, true).getBody();
        return ((int) ((ArrayList<LinkedHashMap<String, Object>>) object).get(0).get("hits"));
    }
}
