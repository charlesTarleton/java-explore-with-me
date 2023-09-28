package ru.practicum.exploreWithMe.adminPackage.service.compilationService;

import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compilationId);

    CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest compilationDto);
}
