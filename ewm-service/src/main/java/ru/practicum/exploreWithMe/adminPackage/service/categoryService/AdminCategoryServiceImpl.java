package ru.practicum.exploreWithMe.adminPackage.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.model.Category;
import ru.practicum.exploreWithMe.commonFiles.category.repository.CategoryRepository;
import ru.practicum.exploreWithMe.commonFiles.category.utils.CategoryMapper;
import ru.practicum.exploreWithMe.commonFiles.exception.fourHundredFour.CategoryExistException;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private static final String SERVICE_LOG = "Сервис категорий администратора получил запрос на {}{}";

    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        log.info(SERVICE_LOG, "добавление новой категории: ", categoryDto);
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    public void deleteCategory(Long categoryId) {
        log.info(SERVICE_LOG, "удаление категории с id: ", categoryId);
        checkCategoryIsExist(categoryId);
        categoryRepository.deleteById(categoryId);
    }

    public CategoryDto updateCategory(Long categoryId, NewCategoryDto categoryDto) {
        log.info(SERVICE_LOG, "изменение категории с id: ", categoryId);
        Category category = checkCategoryIsExist(categoryId);
        if (category.getName().equals(categoryDto.getName())) {
            return CategoryMapper.toDto(category);
        }
        return CategoryMapper.toDto(categoryRepository.save(CategoryMapper.toCategory(categoryDto)));
    }

    private Category checkCategoryIsExist(Long categoryId) {
        log.info("Начата процедура проверки наличия категории с id: {}", categoryId);
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new CategoryExistException("Ошибка. Указанная категория не найдена");
        }
        return category.get();
    }
}
