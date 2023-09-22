package ru.practicum.exploreWithMe.publicPackage.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.event.dto.EventShortDto;
import ru.practicum.exploreWithMe.publicPackage.service.compilationService.PublicCompilationServiceImpl;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PublicCompilationControllerTest {
    @Mock
    private PublicCompilationServiceImpl compilationService;

    @InjectMocks
    private PublicCompilationController compilationController;

    @Test
    void shouldGetCompilation() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(compilationController).build();

        when(compilationService.getCompilation(anyLong()))
                .thenReturn(new CompilationDto(
                        List.of(new EventShortDto()), 4L, true, "Заголовок подборки"));

        mvc.perform(get("/compilations/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.events", containsInAnyOrder(notNullValue())))
                .andExpect(jsonPath("$.id", is(4)))
                .andExpect(jsonPath("$.pinned", is(true)))
                .andExpect(jsonPath("$.title", is("Заголовок подборки")));
    }
}
