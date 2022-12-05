package ru.geekbrains.habr.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.Article2Dto;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ArticleConverter {

    public ArticleDto entityToDto(Article article){
        ArticleDto articleDto = new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getImagePath(),
                article.getUser().getUsername(),
                article.getStatus(),
                "",
                "",
                article.getArticleTotal().getLikesTotal(),
                article.getArticleTotal().getCommentsTotal());
        if(article.getDtPublished()!= null){
            articleDto.setDtPublished(article.getDtPublished().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        if(article.getDtCreated()!= null){
            articleDto.setDtCreated(article.getDtCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        return articleDto;
    }

    public ArticleDto entityToDtoForPage(Article article){
        ArticleDto articleDto = entityToDto(article);
        if(articleDto.getText().length()>50){
            articleDto.setText(article.getText().substring(0,50) + "...");
        }
        return articleDto;
    }
    // TODO Может стоит переименовать метод?
    public Article2Dto entityTo2Dto(Article article) {
        return new Article2Dto(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getUser().getUsername(),
                article.getStatus().getName(),
                article.getDtCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
