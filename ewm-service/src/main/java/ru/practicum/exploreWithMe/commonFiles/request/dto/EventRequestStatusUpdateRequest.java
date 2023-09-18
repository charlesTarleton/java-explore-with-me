package ru.practicum.exploreWithMe.commonFiles.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;
    @NotNull
    private RequestStatus status;
}
