package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllSortDesc() {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "dtPublished"));
    }

    public Article findById(Long idArticle) {
        Optional<Article> optionalArticle = articleRepository.findById(idArticle);
        Article article = new Article();
        if(optionalArticle.isPresent()){
            article = optionalArticle.get();
        }
        return article;
    }
}
