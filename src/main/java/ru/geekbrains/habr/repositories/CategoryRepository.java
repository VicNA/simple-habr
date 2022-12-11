package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT c.* FROM categories c WHERE category_id in (select category_id from article_to_category where article_id = :articleId)", nativeQuery = true)
    List<Category> findAllByArticleId(Long articleId);

    @Query("SELECT c FROM Category c WHERE c.name = :categoryName")
    Category findOneByName(String categoryName);
}
