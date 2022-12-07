package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.CommentConverter;
import ru.geekbrains.habr.dtos.CommentDto;
import ru.geekbrains.habr.dtos.NewCommentDto;
import ru.geekbrains.habr.dtos.ResponseMessage;
import ru.geekbrains.habr.entities.Comment;
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
     * @param  articleId - id статьи
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

    @PostMapping("moderation/ban/{articleId}")
    public ResponseMessage banById(@PathVariable Long articleId) {
        commentService.banById(articleId);
        return new ResponseMessage("Комментарий забанен");
    }
}

