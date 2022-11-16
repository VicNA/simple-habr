package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.Article2Dto;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;

import java.time.format.DateTimeFormatter;

@Component
public class ArticleConverter {

    public ArticleDto entityToDto(Article article){
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getUser(),
                article.getStatus(),
                article.getDtCreated(),
                article.getDtPublished());
    }

    public Article2Dto entityTo2Dto(Article article) {
        return new Article2Dto(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getUser().getUsername(),
                article.getStatus().getName(),
                article.getDtCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}
