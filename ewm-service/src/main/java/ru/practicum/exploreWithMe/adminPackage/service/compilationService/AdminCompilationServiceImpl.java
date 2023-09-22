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
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private static final String SERVER_LOG = "Сервис подборок событий администратора получил запрос на {}{}";

    public CompilationDto addCompilation(NewCompilationDto compilationDto) {
        log.info(SERVER_LOG, "добавление новой подборки событий: ", compilationDto);
        return CompilationMapper
                .toDto(compilationRepository.save(CompilationMapper
                        .toCompilation(compilationDto, getEventSet(compilationDto.getEvents()))));
    }

    public void deleteCompilation(Long compilationId) {
        log.info(SERVER_LOG, "удаление подборки событий с id: ", compilationId);
        checkCompilationIsExist(compilationId);
        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest compilationDto) {
        log.info(SERVER_LOG, "изменение добавленной подборки событий с id: ", compilationId);
        return CompilationMapper
                .toDto(CompilationMapper
                        .toCompilation(compilationId, compilationDto, getEventSet(compilationDto.getEvents())));
    }

    private void checkCompilationIsExist(Long compilationId) {
        log.info("Начата процедура проверки наличия подборки событий с id: {}", compilationId);
        Optional<Compilation> compilation = compilationRepository.findById(compilationId);
        if (compilation.isEmpty()) {
            throw new CompilationExistException("Указанная подборка событий не найдена");
        }
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
