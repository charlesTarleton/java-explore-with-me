package ru.practicum.exploreWithMe.commonFiles.compilation.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.commonFiles.compilation.model.Compilation;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.utils.EventMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class CompilationMapper {
    public Compilation toCompilation(NewCompilationDto compilationDto, Set<Event> eventList) {
        log.info("Начата процедура преобразования нового ДТО в подборку: {}", compilationDto);
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        return new Compilation(null, compilationDto.getPinned(), compilationDto.getTitle(), eventList);
    }

    public CompilationDto toDto(Compilation compilation, Map<Long, List<CommentDto>> commentMap) {
        log.info("Начата процедура преобразования подборки в ДТО: {}", compilation);
        return new CompilationDto(
                compilation.getEvents().stream()
                        .map(event -> EventMapper.toShortDto(event, commentMap.get(event.getId())))
                        .collect(Collectors.toList()),
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public Compilation toCompilation(Long compilationId, UpdateCompilationRequest compilationDto,
                                     Set<Event> eventSet, Compilation oldCompilation) {
        log.info("Начата процедура преобразования измененного ДТО в подборку: {}", compilationDto);
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(oldCompilation.getPinned());
        }
        if (compilationDto.getEvents() == null) {
            compilationDto.setEvents(oldCompilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet()));
        }
        if (compilationDto.getTitle() == null) {
            compilationDto.setTitle(oldCompilation.getTitle());
        }
        return new Compilation(compilationId, compilationDto.getPinned(), compilationDto.getTitle(), eventSet);
    }
}
