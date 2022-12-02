package ru.geekbrains.habr.entities;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import ru.geekbrains.habr.entities.view.ArticleRating;
import ru.geekbrains.habr.entities.view.ArticleTotal;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@ToString
@Table(name = "articles")
public class Article {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "dt_created")
    private LocalDateTime dtCreated;

    @Column(name = "dt_published")
    private LocalDateTime dtPublished;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "article_to_category",
            joinColumns = @JoinColumn(name = "article_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Collection<Category> categories;

    @OneToOne
    @JoinColumn(name = "article_id")
    private ArticleTotal articleTotal;

    @OneToOne
    @JoinColumn(name = "article_id")
    private ArticleRating articleRating;
}
