package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.CommentConverter;
import ru.geekbrains.habr.dtos.CommentDto;
import ru.geekbrains.habr.dtos.NewCommentDto;
import ru.geekbrains.habr.entities.Comment;
import ru.geekbrains.habr.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor

public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{articleId}")
    public List<CommentDto> findAllByArticle(@PathVariable Long articleId) {
        List<Comment> comments = commentService.findByArticleIdOnlyParentComments(articleId);

        return comments.stream().map(CommentConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@RequestBody NewCommentDto newCommentDto) {
        System.out.println(newCommentDto.toString());
        commentService.add(newCommentDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}

