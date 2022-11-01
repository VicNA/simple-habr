package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.converters.CategoryConverter;
import ru.geekbrains.habr.dtos.CategoryDto;
import ru.geekbrains.habr.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryConverter categoryConverter;

    @GetMapping
    public List<CategoryDto> findAll() {
        return categoryService.findAll().stream()
                .map(categoryConverter::entityToDto).collect(Collectors.toList());
    }
}
