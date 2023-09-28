package ru.practicum.exploreWithMe.privatePackage.service.requestService;

import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    public List<ParticipationRequestDto> getUserRequests(Long userId);

    public ParticipationRequestDto addRequest(Long userId, Long eventId);

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId);
}
