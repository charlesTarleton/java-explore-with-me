package ru.practicum.exploreWithMe.publicPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.publicPackage.service.compilationService.PublicCompilationServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PublicCompilationServiceTest {
    @Mock
    private CompilationRepository compilationRepository;

    @InjectMocks
    private PublicCompilationServiceImpl compilationService;

    @Test
    void should() {

    }
}
