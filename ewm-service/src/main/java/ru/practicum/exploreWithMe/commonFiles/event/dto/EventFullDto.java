package ru.practicum.exploreWithMe.commonFiles.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.exploreWithMe.commonFiles.utils.AppDateTimeFormatter.pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = pattern)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(pattern = pattern)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    @JsonFormat(pattern = pattern)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}
