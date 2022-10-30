package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
}
