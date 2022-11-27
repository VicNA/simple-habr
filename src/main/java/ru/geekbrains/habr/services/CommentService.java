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

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public List<Comment> findByArticleIdOnlyParentComments(Long articleId){
        return commentRepository.findByArticleIdAndParentCommentId(articleId, null);
    }

    @Transactional
    public void add(NewCommentDto newCommentDto){
        Comment comment = new Comment();
        User user = userService.findByUsername(newCommentDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Пользователь '%s' не найден", newCommentDto.getUsername())));
        Article article = articleService.findById(newCommentDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Статья с id '%s' не найдена", newCommentDto.getArticleId())));

        comment.setText(newCommentDto.getText());
        comment.setUser(user);
        comment.setArticle(article);

        if(newCommentDto.getParentCommentId()!=null) {
            Optional<Comment> parentComment = findById(newCommentDto.getParentCommentId());
            parentComment.ifPresent(comment::setParentComment);
        }

        commentRepository.save(comment);
    }

    public Optional<Comment> findById(Long id){
        return commentRepository.findById(id);
    }
}
