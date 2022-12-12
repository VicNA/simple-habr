package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.LikeDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Like;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.LikeRepository;
import ru.geekbrains.habr.services.enums.ContentType;
import ru.geekbrains.habr.services.enums.ErrorMessage;
import ru.geekbrains.habr.services.enums.InfoMessage;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final NotificationService notificationService;

    @Transactional
    public void add(LikeDto likeDto) {
        User user = userService.findByUsername(likeDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(),
                                likeDto.getUsername()))
                );

        Article article = articleService.findById(likeDto.getArticleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.ARTICLE_ID_ERROR.getField(), likeDto.getArticleId()))
                );

        String textNotif = String.format(
                InfoMessage.NOTIFICATION_LIKE_INFO.getField(), user.getUsername(), article.getTitle()
        );

        Optional<Like> like = likeRepository.findByUserAndArticle(user, article);

        if (!like.isPresent()) {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setArticle(article);
            likeRepository.save(newLike);

            if (!article.getUser().getUsername().equals(user.getUsername())) {
                notificationService.createNotification(article.getUser().getUsername(), user.getUsername(), textNotif,
                        article.getId(), ContentType.ARTICLE.getField());
            }
        } else {
            deleteLike(like.get());
            Optional<Notification> notification = notificationService.findBySenderAndText(user, textNotif);

            if (notification.isPresent()) {
                notificationService.deleteNotification(notification.get());
            }
        }
    }

    public void deleteLike(Like like) {
        likeRepository.delete(like);
    }
}
