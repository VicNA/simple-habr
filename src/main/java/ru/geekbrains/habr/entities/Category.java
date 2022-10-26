package ru.geekbrains.habr.entities;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "category_name")
    private String name;

//    @ManyToMany
//    @JoinTable(
//            name = "article_to_category",
//            joinColumns = @JoinColumn(name = "category_id"),
//            inverseJoinColumns = @JoinColumn(name = "article_id")
//    )
//    private Collection<Article> articles;
}
