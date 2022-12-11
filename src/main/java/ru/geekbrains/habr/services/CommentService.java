package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.habr.dtos.NewCommentDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Comment;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.CommentRepository;
import ru.geekbrains.habr.services.enums.ContentType;
import ru.geekbrains.habr.services.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с комметариями
 *
 * @author Рожко Алексей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final NotificationService notificationService;


    /**
     * Возвращает список комментариев, написанные к статье.
     * Без "дочерних" комментариев.
     *
     * @param articleId id статьи
     */
    public List<Comment> findByArticleIdOnlyParentComments(Long articleId) {
        return commentRepository.findByArticleIdAndParentCommentId(articleId, null);
    }

    /**
     * Обработка нового комментария, проверка на соответствие требованиям.
     *
     * @param newCommentDto dto комментария
     */
    @Transactional
    public void add(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        User user = userService.findByUsername(newCommentDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Пользователь '%s' не найден", newCommentDto.getUsername())));
        Article article = articleService.findById(newCommentDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Статья с id '%d' не найдена", newCommentDto.getArticleId())));

        comment.setText(newCommentDto.getText());
        comment.setUser(user);
        comment.setArticle(article);
        comment.setBanned(false);

        if (newCommentDto.getParentCommentId() != null) {
            Optional<Comment> parentComment = findById(newCommentDto.getParentCommentId());
            parentComment.ifPresent(comment::setParentComment);
        }

        commentRepository.save(comment);
        notificationService.sendAllNotification(comment);
    }

    /**
     * Поиск комментария по его id.
     *
     * @param id id комментария
     */
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Заменяет переменную banned в сущности Comment на true
     *
     * @param id id комментария
     */
    @Transactional
    public void banById(Long id) {
        Optional<Comment> optional = commentRepository.findById(id);

        if (optional.isPresent()) {
            Comment comment = optional.get();
            comment.setBanned(true);
            commentRepository.save(comment);
        }
    }

}
