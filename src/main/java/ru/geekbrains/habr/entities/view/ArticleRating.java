package ru.geekbrains.habr.entities.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Immutable
@Table(name = "rating_articles_likes")
public class ArticleRating {
    @Id
    @Column(name = "article_id")
    private Long id;

    @Column(name = "rating")
    private Long rating;
}
