package ru.practicum.exploreWithMe.publicPackage.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventFullDto;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.dto.UserShortDto;
import ru.practicum.exploreWithMe.publicPackage.service.eventService.PublicEventServiceImpl;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublicEventControllerTest {
    @Mock
    private PublicEventServiceImpl eventService;

    @InjectMocks
    private PublicEventController eventController;

    @Test
    void shouldGetEvent() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(eventController).build();

        EventFullDto eventDto = new EventFullDto(
        "Аннотация", new CategoryDto(), 2L, LocalDateTime.now().minusDays(1),
                "Описание", LocalDateTime.now().plusDays(2), 3L, new UserShortDto(),
                new Location(), true, 10L, LocalDateTime.now(), true,
                EventState.PUBLISHED, "Заголовок события", 11);

        when(eventService.getEvent(anyLong(), any(HttpServletRequest.class))).thenReturn(eventDto);

        mvc.perform(get("/events/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.annotation", is("Аннотация")))
                .andExpect(jsonPath("$.category", is(notNullValue())))
                .andExpect(jsonPath("$.confirmedRequests", is(2)))
                .andExpect(jsonPath("$.createdOn", is(notNullValue())))
                .andExpect(jsonPath("$.description", is("Описание")))
                .andExpect(jsonPath("$.eventDate", is(notNullValue())))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.initiator", is(notNullValue())))
                .andExpect(jsonPath("$.location", is(notNullValue())))
                .andExpect(jsonPath("$.paid", is(true)))
                .andExpect(jsonPath("$.participantLimit", is(10)))
                .andExpect(jsonPath("$.publishedOn", is(notNullValue())))
                .andExpect(jsonPath("$.requestModeration", is(true)))
                .andExpect(jsonPath("$.state", is("PUBLISHED")))
                .andExpect(jsonPath("$.title", is("Заголовок события")))
                .andExpect(jsonPath("$.views", is(11)));
    }

}
