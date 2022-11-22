package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Like;
import ru.geekbrains.habr.entities.User;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndArticle(User user, Article article);
}
