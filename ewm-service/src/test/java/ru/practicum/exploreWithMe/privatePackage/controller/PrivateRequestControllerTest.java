package ru.practicum.exploreWithMe.privatePackage.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.commonFiles.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.commonFiles.request.utils.RequestStatus;
import ru.practicum.exploreWithMe.privatePackage.service.requestService.PrivateRequestServiceImpl;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PrivateRequestControllerTest {
    @Mock
    private PrivateRequestServiceImpl requestService;

    @InjectMocks
    private PrivateRequestController requestController;

    @Test
    void shouldCancelRequest() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(requestController).build();

        when(requestService.cancelRequest(anyLong(), anyLong()))
                .thenReturn(new ParticipationRequestDto(
                        LocalDateTime.now(), 3L, 1L, 2L, RequestStatus.CANCELED));

        mvc.perform(patch("/users/2/requests/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.created", is(notNullValue())))
                .andExpect(jsonPath("$.event", is(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.requester", is(2)))
                .andExpect(jsonPath("$.status", is("CANCELED")));
    }
}
