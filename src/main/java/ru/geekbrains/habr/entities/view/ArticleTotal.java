package ru.geekbrains.habr.entities.view;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Data
@Immutable
@Table(name = "article_total_likes_comments")
public class ArticleTotal {

    @Id
    @Column(name="article_id")
    private Long articleId;

    @Column(name = "likes_total")
    private Long likesTotal;

    @Column(name = "comments_total")
    private Long commentsTotal;
}