package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.ArticleDto;
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
}
