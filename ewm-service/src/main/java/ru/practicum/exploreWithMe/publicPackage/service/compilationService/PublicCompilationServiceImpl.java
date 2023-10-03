package ru.practicum.exploreWithMe.publicPackage.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.utils.CommentMapper;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.model.Compilation;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.commonFiles.compilation.utils.CompilationMapper;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CompilationExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.COMPILATION_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final CommentRepository commentRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info(COMPILATION_SERVICE_LOG, "поиск подборок событий", "");
        List<Compilation> compilationList = compilationRepository
                .getCompilations(pinned, new ExploreWithMePageable(from, size, Sort.unsorted()));
        Set<Long> eventSet = new HashSet<>();
        compilationList.stream().map(compilation -> compilation.getEvents().stream()
                .map(event -> eventSet.add(event.getId())));
        Map<Long, List<CommentDto>> commentMap = getCommentMap(eventSet);
        return compilationList.stream()
                .map(compilation -> CompilationMapper.toDto(compilation, commentMap))
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilation(Long compilationId) {
        log.info(COMPILATION_SERVICE_LOG, "получение подборки событий с id: ", compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationExistException("Указанная подборка событий не найдена"));
        Map<Long, List<CommentDto>> commentMap = getCommentMap(compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet()));
        return CompilationMapper.toDto(compilation, commentMap);
    }

    private Map<Long, List<CommentDto>> getCommentMap(Set<Long> eventSet) {
        return commentRepository.getEventsComments(new ArrayList<>(eventSet)).stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.groupingBy(CommentDto::getEventId));
    }
}
