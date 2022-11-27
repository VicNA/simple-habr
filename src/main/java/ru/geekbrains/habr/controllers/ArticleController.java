package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.Article2Dto;
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
    public Page<ArticleDto> findAll(@RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {
        if (page < 1) {
            page = 1;
        }

        return articleService.findAllSortDescPage(page - 1).map(articleConverter::entityToDto);
    }

    @GetMapping("/view/{id}")
    public ArticleDto findById(@PathVariable Long id) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Статья с id = '%d' не найдена", id)));

        return articleConverter.entityToDto(article);
    }

    @GetMapping("/category")
    public Page<ArticleDto> findAllByCategoryPage(@RequestParam(value = "id", required = true) Long id,
                                                  @RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {
        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByCategoryPage(id, page - 1).map(articleConverter::entityToDto);
    }

    @GetMapping("/user")
    public Page<ArticleDto> findAllByUsernamePage(@RequestParam(value = "username", required = true) String username,
                                                  @RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {
        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByUsernamePage(username, page - 1).map(articleConverter::entityToDto);
    }

    @PutMapping("/updatePublicFields")
    public void updatePublicFields(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());
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

    @GetMapping("/moderation")
    public List<Article2Dto> findAllByStatus() {
        return articleService.findAllByStatus("moderating").stream()
                .map(articleConverter::entityTo2Dto).collect(Collectors.toList());
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
/*
    @GetMapping("/likes/total/{id}")
    public Long findTotalLikesById(@PathVariable Long id) {
        return articleService.findTotalLikesById(id);
    }

    @GetMapping("/comments/total/{id}")
    public Long findTotalCommentsById(@PathVariable Long id) {
        return articleService.findTotalCommentsById(id);
    }*/
}