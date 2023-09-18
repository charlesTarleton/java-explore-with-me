package ru.practicum.exploreWithMe.publicPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.publicPackage.service.compilationService.PublicCompilationService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final PublicCompilationService publicService;
    private static final String CONTROLLER_LOG = "Публичный контроллер подборок событий получил запрос на {}{}";

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(CONTROLLER_LOG, "поиск подборок событий", "");
        return publicService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") Long compilationId) {
        log.info(CONTROLLER_LOG, "получение подборки событий с id: ", compilationId);
        return publicService.getCompilation(compilationId);
    }
}
