package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.Article2Dto;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.ArticleService;
import ru.geekbrains.habr.services.StatusService;
import ru.geekbrains.habr.services.enums.ArticleStatus;
import ru.geekbrains.habr.services.enums.ErrorMessage;

import java.util.Arrays;
import java.util.List;

/**
 * Контроллер REST API для обработки запросов связанных с статьями
 *
 * @author
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final StatusService statusService;

    private final ArticleConverter articleConverter;

    private Integer getPage(Integer value) {
        return (value < 1 ? 1 : value) - 1;
    }

    /**
     * Возвращает все опубликованные статьи
     *
     * @param page Номер страницы
     * @return Страницу DTO статей
     */
    @GetMapping
    public Page<ArticleDto> findAll(@RequestParam(name = "page") Integer page) {
        return articleService.findAllPage(getPage(page), ArticleStatus.PUBLISHED)
                .map(articleConverter::entityToDtoForPage);
    }

    /**
     * Возвращает опубликованные статьи по фильтру наименовании статьи и указанной сортировкой
     *
     * @param page  Номер страницы
     * @param title Искомое слова в наименовании статьи
     * @param sort  Сортировка
     * @return Страницу DTO статей
     */
    @GetMapping("/findByFilter")
    public Page<ArticleDto> findByFilter(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "title") String title,
            Sort sort
    ) {
        return articleService.findByFilter(getPage(page), ArticleStatus.PUBLISHED, title, sort)
                .map(articleConverter::entityToDtoForPage);
    }

    /**
     * Возвращает статьи отсортированных по рейтингу
     *
     * @return Страницу DTO статей
     */
    @GetMapping("/rating")
    public Page<ArticleDto> findByRating() {
        return articleService.findByRating(5).map(articleConverter::entityToDtoForPage);
    }

    /**
     * Возвращает статью по идентификатору
     *
     * @param id Идентификатор статьи
     * @return DTO статьи
     */
    @GetMapping("/view/{id}")
    public ArticleDto findById(@PathVariable Long id) {
        Article article = articleService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.ARTICLE_ID_ERROR.getField(), id))
                );

        return articleConverter.entityToDto(article);
    }

    /**
     * Возвращает статьи по идентификатору категории
     *
     * @param page Номер страницы
     * @param id   Идентификатор категории
     * @param sort Сортировка
     * @return Страницу DTO статей
     */
    @GetMapping("/category")
    public Page<ArticleDto> findAllByCategoryPage(
            @RequestParam(required = false, defaultValue = "1", name = "page") Integer page,
            @RequestParam(value = "id", required = true) Long id,
            Sort sort) {

        return articleService.findAllByCategoryPage(id, getPage(page), sort)
                .map(articleConverter::entityToDtoForPage);
    }

    /**
     * Возвращает статьи указанного пользователя
     *
     * @param page     Номер страницы
     * @param username Имя пользователя
     * @return Страницу DTO статей
     */
    @GetMapping("/user")
    public Page<ArticleDto> findAllByUsernamePage(
            @RequestParam(defaultValue = "1", name = "page") Integer page,
            @RequestParam(value = "username", required = true) String username) {

        return articleService.findAllByUsernamePage(username, getPage(page))
                .map(articleConverter::entityToDtoForPage);
    }

    /**
     * Update public fields.
     *
     * @param articleDto DTO статьи
     */
    @PutMapping("/updatePublicFields")
    public void updatePublicFields(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName(ArticleStatus.HIDDEN.toString()).orElseThrow());
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }

    /**
     * Update categories.
     *
     * @param articleId  the article id
     * @param categories the categories
     */
    @PutMapping("/{articleId}/updateCategories")
    public void updateCategories(@PathVariable Long articleId,
                                 @RequestParam(name = "categories") String[] categories) {
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }


    /**
     * Update public fields and publicate.
     *
     * @param articleDto the article dto
     */
    @PutMapping("/updatePublicFieldsAndPublicate")
    public void updatePublicFieldsAndPublicate(@RequestBody ArticleDto articleDto) {
        articleDto.setStatus(statusService.findByName(ArticleStatus.MODERATING.toString()).orElseThrow());
        articleService.updateArticlePublicFieldsFromDto(articleDto);
    }

    /**
     * Create article.
     *
     * @param articleDto the article dto
     * @param categories the categories
     */
    @PutMapping("/create")
    public void createArticle(@RequestBody ArticleDto articleDto
                            , @RequestParam(name = "categories") String[] categories) {
        articleDto.setStatus(statusService.findByName("hidden").orElseThrow());
        Long articleId = articleService.createArticleFromDto(articleDto);
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }

    /**
     * Create and publicate.
     *
     * @param articleDto the article dto
     * @param categories the categories
     */
    @PutMapping("/createAndPublicate")
    public void createAndPublicate(@RequestBody ArticleDto articleDto
                                 , @RequestParam(name = "categories") String[] categories) {
        articleDto.setStatus(statusService.findByName(ArticleStatus.MODERATING.toString()).orElseThrow());
        Long articleId = articleService.createArticleFromDto(articleDto);
        List<String> categoriesList = Arrays.asList(categories);
        articleService.updateCategories(articleId, categoriesList);
    }

    /**
     * Find all by status page page.
     *
     * @param page the page
     * @return the page
     */
    @GetMapping("/moderation")
    public Page<Article2Dto> findAllByStatusPage(
            @RequestParam(required = false, defaultValue = "1", name = "page") Integer page) {

        if (page < 1) {
            page = 1;
        }

        return articleService.findAllByStatusPage(ArticleStatus.MODERATING.toString(), page - 1)
                .map(articleConverter::entityTo2Dto);
    }

    /**
     * Update status.
     *
     * @param articleId  the article id
     * @param statusName the status name
     */
    @PutMapping("/moderation/{id}/updateStatus")
    public void updateStatus(@PathVariable(name = "id") Long articleId,
                             @RequestParam(name = "status") String statusName) {

        articleService.updateStatus(articleId, statusName);
    }

    /**
     * Delete article.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}