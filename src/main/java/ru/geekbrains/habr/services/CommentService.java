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
import ru.geekbrains.habr.services.enums.ErrorMessage;

import java.util.List;
import java.util.Optional;

/**
 * Сервис обработки комментариев
 *
 * @author Рожко Алексей
 *
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
     * @param articleId Идентификатор статьи
     * @return Список комментариев
     */
    public List<Comment> findByArticleIdOnlyParentComments(Long articleId) {
        return commentRepository.findByArticleIdAndParentCommentId(articleId, null);
    }

    /**
     * Добавляет новый комментарий, проверяет на соответствие требованиям.
     *
     * @param newCommentDto DTO комментария
     */
    @Transactional
    public void add(NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        User user = userService.findByUsername(newCommentDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(), newCommentDto.getUsername()))
                );

        Article article = articleService.findById(newCommentDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(
                                ErrorMessage.ARTICLE_ID_ERROR.getField(), newCommentDto.getArticleId()))
                );

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
     * Находит комментарий по идентификатору
     *
     * @param id Идентификатор комментария
     * @return The optional
     */
    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    /**
     * Заменяет переменную banned в сущности Comment на true
     *
     * @param id Идентификатор комментария
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
