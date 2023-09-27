package ru.practicum.exploreWithMe.publicPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.publicPackage.service.categoryService.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

import static ru.practicum.exploreWithMe.commonFiles.utils.ConstantaClass.Public.CATEGORY_CONTROLLER_LOG;

@RestController
@Slf4j
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PublicCategoryController {
    private final PublicCategoryService publicService;

    @GetMapping
    public Set<CategoryDto> getCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                              Integer from,
                                          @Positive @RequestParam(value = "size", defaultValue = "10")
                                          Integer size) {
        log.info(CATEGORY_CONTROLLER_LOG, "получение списка категорий", "");
        return publicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryInfo(@PathVariable("catId") Long categoryId) {
        log.info(CATEGORY_CONTROLLER_LOG, "получение категории с id: ", categoryId);
        return publicService.getCategoryInfo(categoryId);
    }
}
