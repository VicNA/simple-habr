package ru.geekbrains.habr.converters;

import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;

public class ArticleConverter {

    public ArticleDto entityToDto(Article article){
        return new ArticleDto(article.getId(), article.getTitle(), article.getText());
    }
}
