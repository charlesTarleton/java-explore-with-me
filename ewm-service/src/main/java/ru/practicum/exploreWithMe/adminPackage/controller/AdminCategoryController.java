package ru.practicum.exploreWithMe.adminPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.adminPackage.service.categoryService.AdminCategoryService;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.commonFiles.category.dto.NewCategoryDto;

import javax.validation.Valid;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Admin.CATEGORY_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info(CATEGORY_CONTROLLER_LOG, "добавление новой категории: ", categoryDto);
        return adminService.addCategory(categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") Long categoryId) {
        log.info(CATEGORY_CONTROLLER_LOG, "удаление категории с id: ", categoryId);
        adminService.deleteCategory(categoryId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategory(@PathVariable("catId") Long categoryId,
                                      @Valid @RequestBody NewCategoryDto categoryDto) {
        log.info(CATEGORY_CONTROLLER_LOG, "изменение категории с id: ", categoryId);
        return adminService.updateCategory(categoryId, categoryDto);
    }
}
