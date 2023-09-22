package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.categoryService.AdminCategoryService;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminService;
    private static final String CONTROLLER_LOG = "Контроллер категорий администратора получил запрос на {}{}";

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info(CONTROLLER_LOG, "добавление новой категории: ", categoryDto);
        return adminService.addCategory(categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") Long categoryId) {
        log.info(CONTROLLER_LOG, "удаление категории с id: ", categoryId);
        adminService.deleteCategory(categoryId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable("catId") Long categoryId,
                                      @Valid @RequestBody NewCategoryDto categoryDto) {
        log.info(CONTROLLER_LOG, "изменение категории с id: ", categoryId);
        return adminService.updateCategory(categoryId, categoryDto);
    }
}
