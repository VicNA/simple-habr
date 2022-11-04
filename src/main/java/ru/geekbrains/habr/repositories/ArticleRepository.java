package ru.geekbrains.habr.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByOrderByDtPublishedDesc();

    @Query("SELECT a FROM Article a JOIN a.categories c WHERE c.id = :categoryId")
    List<Article> findAllByCategory(@Param("categoryId") Long categoryId, Sort sort);
}
