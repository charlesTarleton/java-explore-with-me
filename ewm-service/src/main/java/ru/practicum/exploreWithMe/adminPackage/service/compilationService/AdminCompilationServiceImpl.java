package ru.practicum.exploreWithMe.adminPackage.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.HashSet;
import java.util.Set;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.COMPILATION_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info(COMPILATION_SERVICE_LOG, "добавление новой подборки событий: ", compilationDto);
        return CompilationMapper.toDto(compilationRepository.save(CompilationMapper
                        .toCompilation(compilationDto, getEventSet(compilationDto.getEvents()))));
    }

    public void deleteCompilation(Long compilationId) {
        log.info(COMPILATION_SERVICE_LOG, "удаление подборки событий с id: ", compilationId);
        checkCompilationIsExist(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest compilationDto) {
        log.info(COMPILATION_SERVICE_LOG, "изменение добавленной подборки событий с id: ", compilationId);
        Compilation oldCompilation = checkCompilationIsExist(compilationId);
        return CompilationMapper.toDto(compilationRepository.save(CompilationMapper
                .toCompilation(compilationId, compilationDto,
                        getEventSet(compilationDto.getEvents()),
                        oldCompilation)));
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
}
