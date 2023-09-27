package ru.practicum.exploreWithMe.publicPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.publicPackage.service.compilationService.PublicCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.COMPILATION_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Validated
public class PublicCompilationController {
    private final PublicCompilationService publicService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                @PositiveOrZero
                                                    @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                @Positive
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info(COMPILATION_CONTROLLER_LOG, "поиск подборок событий", "");
        return publicService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") Long compilationId) {
        log.info(COMPILATION_CONTROLLER_LOG, "получение подборки событий с id: ", compilationId);
        return publicService.getCompilation(compilationId);
    }
}
