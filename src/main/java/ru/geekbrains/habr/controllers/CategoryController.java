package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.converters.CategoryConverter;
import ru.geekbrains.habr.dtos.CategoryDto;
import ru.geekbrains.habr.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер обработки запросов к категориям
 *
 * @author Николаев Виктор
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryConverter categoryConverter;

    /**
     * Возвращает все категории
     *
     * @return Список DTO категории
     */
    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll().stream()
                .map(categoryConverter::entityToDto).collect(Collectors.toList());
    }

    /**
     * Возвращает все категории конкретной статьи
     *
     * @param articleId Идентификатор статьи
     * @return Список DTO категории
     */
    @GetMapping("/article/{id}")
    public List<CategoryDto> findAllByArticleId(@PathVariable(name = "id") Long articleId) {
        return categoryService.findAllByArticleId(articleId).stream()
                .map(categoryConverter::entityToDto).collect(Collectors.toList());
    }
}
