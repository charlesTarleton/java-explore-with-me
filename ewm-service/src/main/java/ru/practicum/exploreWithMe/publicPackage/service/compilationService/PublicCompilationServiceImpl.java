package ru.practicum.exploreWithMe.publicPackage.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.compilation.dto.CompilationDto;
import ru.practicum.exploreWithMe.commonFiles.compilation.repository.CompilationRepository;
import ru.practicum.exploreWithMe.commonFiles.compilation.utils.CompilationMapper;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CompilationExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.COMPILATION_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info(COMPILATION_SERVICE_LOG, "поиск подборок событий", "");
        return compilationRepository
                .getCompilations(pinned, new ExploreWithMePageable(from, size, Sort.unsorted())).stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilation(Long compilationId) {
        log.info(COMPILATION_SERVICE_LOG, "получение подборки событий с id: ", compilationId);
        return CompilationMapper.toDto(compilationRepository.findById(compilationId)
                .orElseThrow(() -> new CompilationExistException("Указанная подборка событий не найдена")));
    }
}
