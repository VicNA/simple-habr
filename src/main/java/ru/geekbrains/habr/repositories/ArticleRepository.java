package ru.geekbrains.habr.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

    @Query("SELECT a FROM Article a JOIN a.categories c WHERE c.id = :categoryId and a.status.name = :statusName")
    Page<Article> findAllByCategoryPage(@Param("statusName") String statusName, @Param("categoryId") Long categoryId,
                                        Pageable pageRequest);

    @Query("SELECT a FROM Article a WHERE a.user.username = :username")
    Page<Article> findAllByUsernamePage(@Param("username") String username, Pageable pageRequest);

    @Query("SELECT a FROM Article a WHERE a.status.name = :statusName")
    List<Article> findAllByStatusName(@Param("statusName") String statusName);

    @Query("SELECT a FROM Article a WHERE a.status.name = :statusName")
    Page<Article> findAllByStatusNamePage(@Param("statusName") String statusName, Pageable pageRequest);

    // Почему эти запросы в репозитории Article, а не в своих собственных?
    @Query(value = "select count(1) from likes where article_id = :id", nativeQuery = true)
    Long countLikesById(Long id);

    @Query(value = "select count(1) from comments where article_id = :id", nativeQuery = true)
    Long countCommentsById(Long id);

    /*
        TODO Предпологаемый нативный запрос для выборки статей и сортировки по популярности (по количесту лайков)
        SELECT DISTINCT a.*, COUNT(l.LIKE_ID)
        FROM ARTICLES a
          JOIN LIKES l ON l.ARTICLE_ID = a.ARTICLE_ID
        GROUP BY l.LIKE_ID
        ORDER BY COUNT(l.LIKE_ID) DESC
        TODO Как это преобразовать в виде кода Java непонятно
     */
}