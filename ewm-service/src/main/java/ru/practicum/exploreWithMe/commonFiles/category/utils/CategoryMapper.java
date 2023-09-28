package ru.practicum.exploreWithMe.commonFiles.category.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;

@UtilityClass
@Slf4j
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        log.info("Начата процедура преобразования категории в ДТО: {}", category);
        return new CategoryDto(category.getId(), category.getName());
    }

    public Category toCategory(NewCategoryDto categoryDto) {
        log.info("Начата процедура преобразования ДТО в категорию: {}", categoryDto);
        return new Category(null, categoryDto.getName());
    }
}
