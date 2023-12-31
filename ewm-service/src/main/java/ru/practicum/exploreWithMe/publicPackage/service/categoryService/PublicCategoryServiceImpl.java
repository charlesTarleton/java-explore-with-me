package ru.practicum.exploreWithMe.publicPackage.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.category.utils.CategoryMapper;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CategoryExistException;
import ru.practicum.exploreWithMe.commonFiles.utils.ExploreWithMePageable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.CATEGORY_SERVICE_LOG;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository categoryRepository;

    public Set<CategoryDto> getCategories(Integer from, Integer size) {
        log.info(CATEGORY_SERVICE_LOG, "получение списка категорий", "");
        return categoryRepository
                .findAll(new ExploreWithMePageable(from, size, Sort.by("id").ascending())).stream()
                .map(CategoryMapper::toDto).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public CategoryDto getCategoryInfo(Long categoryId) {
        log.info(CATEGORY_SERVICE_LOG, "получение категории с id: ", categoryId);
        return CategoryMapper.toDto(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryExistException("Указанная категория не найдена")));
    }
}
