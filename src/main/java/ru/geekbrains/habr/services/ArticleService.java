package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.repositories.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllSortDesc() {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC, "dtPublished"));
    }
}
