package ru.practicum.exploreWithMe.adminPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.adminPackage.service.categoryService.AdminCategoryServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminCategoryServiceImpl categoryService;

    @Test
    void shouldAddCategory() {
        when(categoryRepository.save(any(Category.class)))
                .thenReturn((new Category(1L, "Категория")));
        CategoryDto category = categoryService.addCategory(new NewCategoryDto("Категория"));
        assertEquals(1L, category.getId());
        assertEquals("Категория", category.getName());
    }
}
