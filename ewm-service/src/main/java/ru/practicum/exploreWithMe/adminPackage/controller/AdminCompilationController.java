package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.compilationService.AdminCompilationServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.UpdateCompilationRequest;

@RestController
@Slf4j
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class AdminCompilationController {
    private final AdminCompilationServiceImpl adminService;
    private static final String CONTROLLER_LOG = "Контроллер подборок событий администратора получил запрос на {}{}";

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationDto addCompilation(@RequestBody NewCompilationDto compilationDto) {
        log.info(CONTROLLER_LOG, "добавление новой подборки событий: ", compilationDto);
        return adminService.addCompilation(compilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable("compId") Long compilationId) {
        log.info(CONTROLLER_LOG, "удаление подборки событий с id: ", compilationId);
        adminService.deleteCompilation(compilationId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long compilationId,
                                    @RequestBody UpdateCompilationRequest compilationDto) {
        log.info(CONTROLLER_LOG, "изменение добавленной подборки событий с id: ", compilationId);
        return adminService.updateCompilation(compilationId, compilationDto);
    }
}
