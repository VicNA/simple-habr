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

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final NotificationService notificationService;


    public List<Comment> findByArticleIdOnlyParentComments(Long articleId) {
        return commentRepository.findByArticleIdAndParentCommentId(articleId, null);
    }

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

        sendAllNotification(comment);
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void banById(Long id){
        Optional<Comment> optional= commentRepository.findById(id);
        if(optional.isPresent()){
            Comment comment = optional.get();
            comment.setBanned(true);
            commentRepository.save(comment);
        }
    }

    private void sendAllNotification(Comment comment) {

        String textNotif = String.format("Пользователь %s добавил комментарий к Вашей статье <<%s>>",
                comment.getUser().getUsername(),comment.getArticle().getTitle());
        notificationService.createNotification(
                comment.getArticle().getUser().getUsername(), comment.getUser().getUsername(), textNotif,
                comment.getArticle().getId(), ContentType.ARTICLE.getField());

        String nameRole = "@moderator";

        if(comment.getText().toLowerCase().contains(nameRole)){

            String whereName = "к статье";
            String whereId = comment.getArticle().getId().toString();
            String message = comment.getText().replace(nameRole, "");
            Long contentId = comment.getArticle().getId();
            String contentType = ContentType.ARTICLE.getField();

                if(comment.getParentComment()!=null){
                    whereName = "к комментарию";
                    whereId = comment.getParentComment().getId().toString();
                    contentId = comment.getParentComment().getId();
                    contentType = ContentType.COMMENT.getField();
                }

            String textNotifForModer = String.format("Пользователь %s призвал Вас %s <<%s>> c формулировкой: \"%s\"",
                    comment.getUser().getUsername(), whereName, whereId, message);
            notificationService.createNotificationForSpecificRole(
                    UserRole.ROLE_MODERATOR, comment.getUser().getUsername(), textNotifForModer, contentId, contentType);
        }
    }

}
