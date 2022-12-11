package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.CommentConverter;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.dtos.CommentDto;
import ru.geekbrains.habr.dtos.NewCommentDto;
import ru.geekbrains.habr.dtos.ResponseMessage;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Comment;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер REST API комметариев
 *
 * @author Рожко Алексей
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor

public class CommentController {
    private final CommentService commentService;

    /**
     * Возвращает комментарии статьи
     *
     * @param articleId - id статьи
     * @return Список dto комментариев статьи
     */
    @GetMapping("/{articleId}")
    public List<CommentDto> findAllByArticle(@PathVariable Long articleId) {
        List<Comment> comments = commentService.findByArticleIdOnlyParentComments(articleId);

        return comments.stream().map(CommentConverter::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет новый комментарий к статье
     *
     * @param newCommentDto Данные нового комметария
     * @return ResponseEntity с статусом 200
     */
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody NewCommentDto newCommentDto) {
        commentService.add(newCommentDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Изменяет статус комментария на "забанен"
     *
     * @param commentId id комментария
     * @return ResponseMessage с успешным ответом
     */

    @PostMapping("moderation/ban/{commentId}")
    public ResponseMessage banById(@PathVariable Long commentId) {
        commentService.banById(commentId);
        return new ResponseMessage("Комментарий забанен");
    }

    /**
     * Возвращает id статьи по комментарию,
     * который в ней написан
     *
     * @param commentId - id комментария
     * @return id статьи
     */
    @GetMapping("/getArticleId/{commentId}")
    public Long findArticleIdByCommentId(@PathVariable Long commentId) {
        return commentService.findById(commentId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format("Комментарий с id = '%d' не найден", commentId)))
                .getArticle().getId();
    }
}