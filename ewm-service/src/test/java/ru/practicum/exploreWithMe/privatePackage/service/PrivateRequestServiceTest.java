package ru.practicum.exploreWithMe.privatePackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.commonFiles.request.model.Request;
import ru.practicum.exploreWithMe.commonFiles.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.commonFiles.user.repository.UserRepository;
import ru.practicum.exploreWithMe.privatePackage.service.requestService.PrivateRequestServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrivateRequestServiceTest {
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PrivateRequestServiceImpl requestService;

    @Test
    void shouldCancelRequest() {
        User initiator = new User(4L, "Юзер", "email2@mail.ru");
        Request request = new Request(
                2L,
                LocalDateTime.now(),
                new Event(3L,
                        "Аннотация", new Category(), 2L,
                        LocalDateTime.now().minusDays(1), "Описание",
                        LocalDateTime.now().plusDays(2), initiator, new Location(), true, 10L,
                        LocalDateTime.now(), true, EventState.PUBLISHED,
                        "Заголовок события", 11),
                new User(1L, "Имя", "email1@mail.ru"),
                RequestStatus.CANCELED);

        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(initiator));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        ParticipationRequestDto requestDto = requestService.cancelRequest(1L, 3L);
        assertEquals(2L, requestDto.getId());
        assertEquals(RequestStatus.CANCELED, requestDto.getStatus());
    }
}
