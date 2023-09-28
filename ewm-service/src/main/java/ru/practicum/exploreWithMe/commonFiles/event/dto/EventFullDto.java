package ru.practicum.exploreWithMe.commonFiles.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Common.PATTERN;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    @JsonUnwrapped
    private EventShortDto eventShortDto;

    @JsonFormat(pattern = PATTERN)
    private LocalDateTime createdOn;

    private String description;

    @Valid
    private LocationDto location;

    private Long participantLimit;

    @JsonFormat(pattern = PATTERN)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;
}
