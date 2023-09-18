package ru.practicum.exploreWithMe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.controller.StatsServerController;
import ru.practicum.exploreWithMe.dto.EndpointHitDto;
import ru.practicum.exploreWithMe.dto.ViewStatsDto;
import ru.practicum.exploreWithMe.service.StatsServerService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class StatsServerControllerTest {
    @Mock
    private StatsServerService statisticService;
    @InjectMocks
    private StatsServerController statisticController;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(statisticController)
                .build();
    }

    @Test
    void shouldSaveStatistic() throws Exception {
        EndpointHitDto endpointHitDto = new EndpointHitDto("uri", "ip", LocalDateTime.now());
        mvc.perform(post("/hit")
                        .content(mapper.writeValueAsString(endpointHitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetStatistic() throws Exception {
        String startStr = LocalDateTime.now().minusDays(1).toString();
        String endStr = LocalDateTime.now().plusDays(1).toString();
        String[] uris = new String[]{"uri"};
        when(statisticService.getStatistic(startStr, endStr, uris, false))
                .thenReturn(List.of(new ViewStatsDto("app", "uri", 1)));
        mvc.perform(get("/stats")
                        .param("start", startStr)
                        .param("end", endStr)
                        .param("uris", uris)
                        .param("unique", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].app", containsInAnyOrder("app")))
                .andExpect(jsonPath("$[*].uri", containsInAnyOrder("uri")))
                .andExpect(jsonPath("$[*].hits", containsInAnyOrder(1)));
    }
}