package ru.practicum.exploreWithMe.publicPackage.service.categoryService;

import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;

import java.util.Set;

public interface PublicCategoryService {
    Set<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryInfo(Long categoryId);
}
