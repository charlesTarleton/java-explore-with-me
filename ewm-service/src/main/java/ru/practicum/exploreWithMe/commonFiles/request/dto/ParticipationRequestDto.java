package ru.practicum.exploreWithMe.commonFiles.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;

import java.time.LocalDateTime;

import static ru.practicum.exploreWithMe.commonFiles.utils.AppDateTimeFormatter.pattern;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = pattern)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;

    private RequestStatus status;
}
