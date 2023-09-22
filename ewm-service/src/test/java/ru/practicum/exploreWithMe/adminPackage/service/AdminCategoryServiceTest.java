package ru.practicum.exploreWithMe.adminPackage.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.exploreWithMe.adminPackage.service.categoryService.AdminCategoryServiceImpl;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;

@ExtendWith(MockitoExtension.class)
public class AdminCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private AdminCategoryServiceImpl categoryService;

    @Test
    void should() {

    }
}
