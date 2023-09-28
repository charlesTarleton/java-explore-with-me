package ru.practicum.exploreWithMe.adminPackage.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.comment.dto.CommentDto;
import ru.practicum.exploreWithMe.commonFiles.comment.repository.CommentRepository;
import ru.practicum.exploreWithMe.commonFiles.comment.utils.CommentMapper;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.commonFiles.compilation.model.Compilation;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.commonFiles.compilation.utils.CompilationMapper;
import ru.practicum.exploreWithMe.commonFiles.event.model.Event;
import ru.practicum.exploreWithMe.commonFiles.event.repository.EventRepository;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CompilationExistException;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.EventExistException;

import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.COMPILATION_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info(COMPILATION_SERVICE_LOG, "добавление новой подборки событий: ", compilationDto);
        Map<Long, List<CommentDto>> commentMap = getCommentMap(compilationDto.getEvents());
        return CompilationMapper.toDto(compilationRepository.save(CompilationMapper
                        .toCompilation(compilationDto, getEventSet(compilationDto.getEvents()))), commentMap);
    }

    public void deleteCompilation(Long compilationId) {
        log.info(COMPILATION_SERVICE_LOG, "удаление подборки событий с id: ", compilationId);
        checkCompilationIsExist(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest compilationDto) {
        log.info(COMPILATION_SERVICE_LOG, "изменение добавленной подборки событий с id: ", compilationId);
        Compilation oldCompilation = checkCompilationIsExist(compilationId);
        Map<Long, List<CommentDto>> commentMap = getCommentMap(compilationDto.getEvents());
        return CompilationMapper.toDto(compilationRepository.save(CompilationMapper
                .toCompilation(compilationId, compilationDto,
                        getEventSet(compilationDto.getEvents()),
                        oldCompilation)), commentMap);
    }

    private Compilation checkCompilationIsExist(Long compilationId) {
        log.info("Начата процедура проверки наличия подборки событий с id: {}", compilationId);
        return compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationExistException("Указанная подборка событий не найдена"));
    }

    private Set<Event> getEventSet(Set<Long> events) {
        log.info("Начата процедура проверки и получения событий из id-коллекции");
        if (events == null) {
            return Set.of();
        }
        Set<Event> eventSet = new HashSet<>(eventRepository.getEventsById(events));
        if (eventSet.size() != events.size()) {
            throw new EventExistException("Как минимум одно из указанных событий не найдено");
        }
        return eventSet;
    }

    private Map<Long, List<CommentDto>> getCommentMap(Set<Long> eventSet) {
        log.info("Начата процедура получения Map из списка событий {}", eventSet);
        if (eventSet != null) {
            Map<Long, List<CommentDto>> commentMap = commentRepository.getEventsComments(new ArrayList<>(eventSet))
                    .stream()
                    .map(CommentMapper::toDto)
                    .collect(Collectors.groupingBy(CommentDto::getEventId));
        }
        return Map.of();
    }
}
