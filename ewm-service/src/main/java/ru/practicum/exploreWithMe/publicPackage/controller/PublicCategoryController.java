package ru.practicum.exploreWithMe.publicPackage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.commonFiles.category.dto.CategoryDto;
import ru.practicum.exploreWithMe.publicPackage.service.categoryService.PublicCategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService publicService;
    private static final String CONTROLLER_LOG = "Публичный контроллер категорий получил запрос на {}{}";

    @GetMapping
    public Set<CategoryDto> getCategories(@PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                              Integer from,
                                          @Positive @RequestParam(value = "size", defaultValue = "10")
                                          Integer size) {
        log.info(CONTROLLER_LOG, "получение списка категорий", "");
        return publicService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryInfo(@PathVariable("catId") Long categoryId) {
        log.info(CONTROLLER_LOG, "получение категории с id: ", categoryId);
        return publicService.getCategoryInfo(categoryId);
    }
}
