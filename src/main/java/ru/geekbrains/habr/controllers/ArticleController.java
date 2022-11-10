package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.ArticleService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleConverter articleConverter;

    @GetMapping
    public List<ArticleDto> findAll() {
        return articleService.findAllSortDesc().stream()
                .map(articleConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/view/{id}")
    public ArticleDto findById(@PathVariable Long id) {
        Article article = articleService.findById(id).orElseThrow(() -> new ResourceNotFoundException("Статья с id = " + id + " не найдена"));
        return articleConverter.entityToDto(article);
    }

    @GetMapping("/category/{id}")
    public List<ArticleDto> findAllByCategory(@PathVariable Long id) {
        return articleService.findAllByCategory(id).stream()
                .map(articleConverter::entityToDto).collect(Collectors.toList());
    }

}
