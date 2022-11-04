package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.repositories.ArticleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> findAllSortDesc() {
        return articleRepository.findByOrderByDtPublishedDesc();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAllByCategory(Long id) {
        return articleRepository.findAllByCategory(id, Sort.by("dtPublished").descending());
    }
}
