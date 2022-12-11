package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.Article2Dto;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.ArticleService;
import ru.geekbrains.habr.services.StatusService;
import ru.geekbrains.habr.services.enums.ArticleStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleConverter articleConverter;
    private final StatusService statusService;

    private Integer getPage(Integer value) {
        return (value < 1 ? 1 : value) - 1;
    }

    @GetMapping
    public Page<ArticleDto> findAll(@RequestParam(name = "page") Integer page) {
        return articleService.findAllPage(getPage(page), ArticleStatus.PUBLISHED)
                .map(articleConverter::entityToDtoForPage);
    }

    @GetMapping("/findByFilter")
    public Page<ArticleDto> findByFilter(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "title") String title,
            Sort sort
    ) {
        return articleService.findAllPage(getPage(page), ArticleStatus.PUBLISHED, title, sort)
                .map(articleConverter::entityToDtoForPage);
    }

    @GetMapping("/rating")
    public Page<ArticleDto> findByRating() {
        return articleService.findByRating(5).map(articleConverter::entityToDtoForPage);
    }

    @GetMapping("/view/{id}")
    public ArticleDto findById(@PathVariable Long id) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Статья с id = '%d' не найдена", id)));

        return articleConverter.entityToDto(article);
    }

    @GetMapping("/category")
    public Page<ArticleDto> findAllByCategoryPage(
            @RequestParam(value = "id", required = true) Long id,
            @RequestParam(required = false, defaultValue = "1", name = "page") Integer page,
            Sort sort) {

        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByCategoryPage(id, page - 1, sort).map(articleConverter::entityToDtoForPage);
    }

    @GetMapping("/user")
    public Page<ArticleDto> findAllByUsernamePage(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {

        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByUsernamePage(username, page - 1).map(articleConverter::entityToDtoForPage);
    }

    @PutMapping("/updatePublicFields")
    public void updatePublicFields(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }

    @PutMapping("/{articleId}/updateCategories")
    public void updateCategories(@PathVariable Long articleId,
                                 @RequestParam(name = "categories") String[] categories) {
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }


    @PutMapping("/updatePublicFieldsAndPublicate")
    public void updatePublicFieldsAndPublicate(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("moderating").orElseThrow());
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }

    @PutMapping("/create")
    public void createArticle(@RequestBody ArticleDto articleDto
                            , @RequestParam(name = "categories") String[] categories) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());
        Long articleId = articleService.createArticleFromDto(articleDto);
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }

    @PutMapping("/createAndPublicate")
    public void createAndPublicate(@RequestBody ArticleDto articleDto
                                 , @RequestParam(name = "categories") String[] categories) {
        articleDto.setStatus(statusService.findByName("moderating").orElseThrow());
        Long articleId = articleService.createArticleFromDto(articleDto);
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }

    @GetMapping("/moderation")
    public Page<Article2Dto> findAllByStatusPage(
            @RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {

        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByStatusPage("moderating", page - 1)
                .map(articleConverter::entityTo2Dto);
    }

    @PutMapping("/moderation/{id}/updateStatus")
    public void updateStatus(@PathVariable(name = "id") Long articleId,
                             @RequestParam(name = "status") String statusName) {

        articleService.updateStatus(articleId, statusName);
    }

    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Статья с id = '%d' не найдена", id)));
        articleService.deleteArticle(article);
    }
}