package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.ArticleService;
import ru.geekbrains.habr.services.StatusService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleConverter articleConverter;
    private final StatusService statusService;

    @GetMapping
    public List<ArticleDto> findAll() {
        return articleService.findAllSortDesc().stream()
                .map(articleConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/view/{id}")
    public ArticleDto findById(@PathVariable Long id) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Статья с id = " + id + " не найдена"));

        return articleConverter.entityToDto(article);
    }

    @GetMapping("/category/{id}")
    public List<ArticleDto> findAllByCategory(@PathVariable Long id) {
        return articleService.findAllByCategory(id).stream()
                .map(articleConverter::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/username/{username}")
    public List<ArticleDto> findAllByUsername(@PathVariable String username) {
        return articleService.findAllByUsername(username)
                .stream()
                .map(articleConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/updatePublicFields")
    public void updatePublicFields(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());// Статусы: 1-hidden, 2-moderating, 3-published
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }


    @PutMapping("/updatePublicFieldsAndPublicate")
    public void updatePublicFieldsAndPublicate(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("moderating").orElseThrow());
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }

    @PutMapping("/create")
    public void createArticle(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());
        articleService.createArticleFromDto(articleDto);
    }

    @PutMapping("/createAndPublicate")
    public void createAndPublicate(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("moderating").orElseThrow());
        articleService.createArticleFromDto(articleDto);
    }

    @GetMapping("/status/moderating")
    public List<ArticleDto> findAllByStatus() {
        return articleService.findAllByStatus("moderating").stream()
                .map(articleConverter::entityToDto).collect(Collectors.toList());
    }
}
