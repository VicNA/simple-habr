package ru.geekbrains.habr.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;

import java.util.List;
import java.util.Optional;

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

    @Modifying
    @Query(value = "delete from article_to_category where article_id = :articleId", nativeQuery = true)
    void clearCategories(Long articleId);

    @Modifying
    @Query(value = "insert into article_to_category(article_id, category_id) values(:articleId, :categoryId)", nativeQuery = true)
    void addToCategory(Long articleId, Long categoryId);
}