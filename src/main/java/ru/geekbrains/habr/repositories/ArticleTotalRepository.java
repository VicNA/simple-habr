package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.ArticleTotal;

@Repository
public interface ArticleTotalRepository extends JpaRepository<ArticleTotal, Long>  {
}



