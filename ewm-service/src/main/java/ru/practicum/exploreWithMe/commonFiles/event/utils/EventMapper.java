package ru.practicum.exploreWithMe.commonFiles.event.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.utils.CategoryMapper;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.*;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.utils.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
@Slf4j
public class EventMapper {
    public Event toEvent(Long eventId, Category category, NewEventDto eventDto, Long confirmedRequests,
                         User user, Location location, Integer views) {
        log.info("Начата процедура преобразования нового ДТО в событие: {}", eventDto);
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0L);
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }
        return new Event(
                eventId,
                eventDto.getAnnotation(),
                category,
                confirmedRequests,
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                user,
                location,
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                null,
                eventDto.getRequestModeration(),
                EventState.PENDING,
                eventDto.getTitle(),
                views);
    }

    public Event toEvent(Long eventId, Category category, UpdateEventUserRequest eventDto,
                         User user, Location location, EventState eventState, Event oldEvent) {
        log.info("Начата процедура преобразования измененного пользователем ДТО в событие: {}", eventDto);
        if (eventDto.getAnnotation() == null) {
            eventDto.setAnnotation(oldEvent.getAnnotation());
        }
        if (eventDto.getCategory() == null) {
            eventDto.setCategory(oldEvent.getCategory().getId());
        }
        if (eventDto.getDescription() == null) {
            eventDto.setDescription(oldEvent.getDescription());
        }
        if (eventDto.getEventDate() == null) {
            eventDto.setEventDate(oldEvent.getEventDate());
        }
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(oldEvent.getPaid());
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(oldEvent.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(oldEvent.getRequestModeration());
        }
        if (eventDto.getTitle() == null) {
            eventDto.setTitle(oldEvent.getTitle());
        }
        return new Event(
                eventId,
                eventDto.getAnnotation(),
                category,
                oldEvent.getConfirmedRequests(),
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                user,
                location,
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                null,
                eventDto.getRequestModeration(),
                eventState,
                eventDto.getTitle(),
                oldEvent.getViews());
    }

    public Event toEvent(Long eventId, Category category, UpdateEventAdminRequest eventDto,
                         Event oldEvent, Location location, EventState eventState) {
        log.info("Начата процедура преобразования измененного администратором ДТО в событие: {}", eventDto);
        if (eventDto.getAnnotation() == null) {
            eventDto.setAnnotation(oldEvent.getAnnotation());
        }
        if (eventDto.getCategory() == null) {
            eventDto.setCategory(oldEvent.getCategory().getId());
        }
        if (eventDto.getDescription() == null) {
            eventDto.setDescription(oldEvent.getDescription());
        }
        if (eventDto.getEventDate() == null) {
            eventDto.setEventDate(oldEvent.getEventDate());
        }
        if (eventDto.getPaid() == null) {
            eventDto.setPaid(oldEvent.getPaid());
        }
        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(oldEvent.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(oldEvent.getRequestModeration());
        }
        if (eventDto.getTitle() == null) {
            eventDto.setTitle(oldEvent.getTitle());
        }
        return new Event(
                eventId,
                eventDto.getAnnotation(),
                category,
                oldEvent.getConfirmedRequests(),
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                oldEvent.getInitiator(),
                location,
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                LocalDateTime.now(),
                eventDto.getRequestModeration(),
                eventState,
                eventDto.getTitle(),
                oldEvent.getViews());
    }

    public EventFullDto toFullDto(Event event, List<CommentDto> commentDtoList) {
        log.info("Начата процедура преобразования события в расширенное ДТО: {}", event);
        return new EventFullDto(
                toShortDto(event, commentDtoList),
                event.getCreatedOn(),
                event.getDescription(),
                LocationMapper.toDto(event.getLocation()),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getStateAction());
    }

    public EventShortDto toShortDto(Event event, List<CommentDto> commentDtoList) {
        log.info("Начата процедура преобразования события в сокращенное ДТО: {}", event);
        if (commentDtoList == null) {
            commentDtoList = List.of();
        }
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews(),
                commentDtoList);
    }
}
