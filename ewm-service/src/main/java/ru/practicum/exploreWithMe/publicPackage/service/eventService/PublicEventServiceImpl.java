package ru.practicum.exploreWithMe.publicPackage.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.exploreWithMe.client.StatsClient;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventSort;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredNine.EventDateTimeException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.AppDateTimeFormatter;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;
//import ru.practicum.exploreWithMe.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
//    private final StatsClient client;
    private static final String SERVICE_LOG = "Публичный сервис событий получил запрос на {}{}";

    public List<EventShortDto> getEventsWithFiltration(String text, Long[] categories, Boolean paid,
                                                       String rangeStartStr, String rangeEndStr,
                                                       Boolean onlyAvailable, EventSort sort, Integer from,
                                                       Integer size, HttpServletRequest request) {
        log.info(SERVICE_LOG, "поиск событий по фильтру пользователя", "");
//        client.saveStatistic(new EndpointHitDto(request.getRequestURI(),
//                request.getRemoteAddr(), LocalDateTime.now()));
        LocalDateTime rangeStart = AppDateTimeFormatter.toDateTime(rangeStartStr);
        LocalDateTime rangeEnd = AppDateTimeFormatter.toDateTime(rangeEndStr);
        if (rangeStart == null || rangeEnd == null) {
            rangeStart = null;
            rangeEnd = null;
        }
        checkSearchDateTime(rangeStart, rangeEnd);
        Pageable pageable;
        switch (sort) {
            case EVENT_DATE:
                pageable = new ExploreWithMePageable(from, size, Sort.by("eventDate").descending());
                break;
            case VIEWS:
                pageable = new ExploreWithMePageable(from, size, Sort.by("views").descending());
                break;
            default:
                pageable = new ExploreWithMePageable(from, size, Sort.unsorted());
                break;
        }
        return eventRepository.getEventsWithFiltration(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, pageable).stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
//        client.saveStatistic(new EndpointHitDto(request.getRequestURI(),
//                request.getRemoteAddr(), LocalDateTime.now()));
        return EventMapper.toFullDto(checkEventIsExist(eventId));
    }

    private void checkSearchDateTime(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        log.info("Начата процедура проверки на противоречия между датой начала и датой окончания поиска");
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new EventDateTimeException("Начало диапазона не может быть позднее его окончания");
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
}
