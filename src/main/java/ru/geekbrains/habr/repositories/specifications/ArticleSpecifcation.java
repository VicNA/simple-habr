package ru.geekbrains.habr.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.habr.entities.Article;

public class ArticleSpecifcation {

    public static Specification<Article> statusEquals(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("status").get("name"), status);
    }

    public static Specification<Article> titleLike(String titlePart) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart));
    }
}
