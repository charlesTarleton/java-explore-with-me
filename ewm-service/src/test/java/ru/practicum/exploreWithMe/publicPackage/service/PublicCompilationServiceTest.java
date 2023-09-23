package ru.practicum.exploreWithMe.publicPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.model.Compilation;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.model.Location;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventState;
import ru.practicum.exploreWithMe.commonFiles.user.model.User;
import ru.practicum.exploreWithMe.publicPackage.service.compilationService.PublicCompilationServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicCompilationServiceTest {
    @Mock
    private CompilationRepository compilationRepository;

    @InjectMocks
    private PublicCompilationServiceImpl compilationService;

    @Test
    void shouldGetCompilation() {
        when(compilationRepository.findById(anyLong()))
                .thenReturn(Optional.of(new Compilation(
                        1L,
                        true,
                        "Заголовок",
                        Set.of(new Event(3L,
                                "Аннотация", new Category(), 2L,
                                LocalDateTime.now().minusDays(1), "Описание",
                                LocalDateTime.now().plusDays(2), new User(1L, "Имя", "email1@mail.ru"),
                                new Location(), true, 10L,
                                LocalDateTime.now(), true, EventState.PUBLISHED,
                                "Заголовок события", 11)))));
        CompilationDto compilationDto = compilationService.getCompilation(1L);
        assertEquals(1L, compilationDto.getId());
        assertEquals(true, compilationDto.getPinned());
        assertEquals("Заголовок", compilationDto.getTitle());
        assertEquals(1, compilationDto.getEvents().size());
        assertEquals(3L, compilationDto.getEvents().get(0).getId());
    }
}
