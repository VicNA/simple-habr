package ru.geekbrains.habr.repositories.specifications;

import org.springframework.data.jpa.domain.Specification;
import ru.geekbrains.habr.entities.Article;

/**
 * Спецификация сущности статей
 *
 * @author Николаев Виктор
 *
 * @version 1.0
 */
public class ArticleSpecifcation {

    /**
     * Поиск по определенному статусу
     *
     * @param status Статус статьи
     * @return Спецификацию
     */
    public static Specification<Article> statusEquals(String status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("status").get("name"), status);
    }

    /**
     * Поиск по совпадению части заголовка
     *
     * @param titlePart Искомое слово
     * @return Спецификацию
     */
    public static Specification<Article> titleLike(String titlePart) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), String.format("%%%s%%", titlePart).toLowerCase());
    }
}
