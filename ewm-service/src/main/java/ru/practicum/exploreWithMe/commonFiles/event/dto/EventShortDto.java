package ru.practicum.exploreWithMe.commonFiles.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Common.PATTERN;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private String annotation;

    @Valid
    private CategoryDto category;

    private Long confirmedRequests;

    @JsonFormat(pattern = PATTERN)
    private LocalDateTime eventDate;

    private Long id;

    @Valid
    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;

    private List<CommentDto> comments;
}
