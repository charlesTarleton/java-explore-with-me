package ru.practicum.exploreWithMe.adminPackage.service.categoryService;

import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;

import java.sql.SQLException;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto categoryDto);

    void deleteCategory(Long categoryId);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto categoryDto);
}
