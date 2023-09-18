package ru.practicum.exploreWithMe.commonFiles.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
