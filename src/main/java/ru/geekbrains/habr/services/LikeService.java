package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.LikeDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Like;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.repositories.ArticleRepository;
import ru.geekbrains.habr.repositories.LikeRepository;
import ru.geekbrains.habr.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public void add(LikeDto likeDto) {
        User user = userRepository.findByUsername(likeDto.getUsername()).orElse(null);
        Article article = articleRepository.findById(likeDto.getArticleId()).orElse(null);

        if (user == null || article == null) return;

        Like like = likeRepository.findByUserAndArticle(user, article).orElse(null);

        if (like == null) {
            like = new Like();
            like.setUser(user);
            like.setArticle(article);

            likeRepository.save(like);
        } else {
            likeRepository.delete(like);
        }
    }
}
