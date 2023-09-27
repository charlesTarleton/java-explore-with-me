package ru.practicum.exploreWithMe.publicPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import ru.practicum.exploreWithMe.client.StatsClient;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.publicPackage.service.eventService.PublicEventServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicEventServiceTest {
    @Mock
    private EventRepository eventRepository;

    @Mock
    private StatsClient client;

    @InjectMocks
    private PublicEventServiceImpl eventService;

    @Test
    void shouldGetEvent() {
        Event event = new Event(1L, "Аннотация", new Category(3L, "Категория"),
                2L, LocalDateTime.now().minusDays(1), "Описание",
                LocalDateTime.now().plusDays(2), new User(2L, "Имя", "email1@mail.ru"),
                new Location(), true, 10L, LocalDateTime.now(), true,
                EventState.PUBLISHED, "Заголовок события", 11);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("app", "ewm");
        map.put("uri", "/event/1");
        map.put("hits", 11);
        List<Object> list = new ArrayList<>();
        list.add(map);
        ResponseEntity<Object> response = new ResponseEntity<>(list, HttpStatus.OK);
        MockHttpServletRequest request = new MockHttpServletRequest();

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(client.getStatistic(any(), any(), any(), anyBoolean())).thenReturn(response);

        EventFullDto eventDto = eventService.getEvent(1L, request);
        assertEquals(1L, eventDto.getEventShortDto().getId());
        assertEquals("Аннотация", eventDto.getEventShortDto().getAnnotation());
        assertEquals(3L, eventDto.getEventShortDto().getCategory().getId());
        assertEquals(2L, eventDto.getEventShortDto().getConfirmedRequests());
        assertEquals(2L, eventDto.getEventShortDto().getInitiator().getId());
        assertEquals("Описание", eventDto.getDescription());
        assertEquals(11, eventDto.getEventShortDto().getViews());
    }
}
