package ru.geekbrains.habr.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a JOIN a.categories c WHERE c.id = :categoryId")
    Page<Article> findAllByCategoryPage(@Param("categoryId") Long categoryId, Pageable pageRequest);

    @Query("SELECT a,s FROM Article a JOIN a.user u JOIN a.status s WHERE u.username = :username")
    List<Article> findAllByUsername(@Param("username") String username, Sort sort);

    @Query("SELECT a FROM Article a JOIN a.status s WHERE s.name = :statusName")
    List<Article> findAllByStatusName(@Param("statusName") String statusName);

    @Query("SELECT a FROM Article a JOIN a.status s WHERE s.name = :statusName")
    Page<Article> findAllByStatusNamePage(@Param("statusName") String statusName, Pageable pageRequest);
}