package ru.practicum.exploreWithMe.publicPackage.service.compilationService;

import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compilationId);
}
