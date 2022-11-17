package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.ArticleRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final StatusService statusService;

    public List<Article> findAllSortDesc() {
        return articleRepository.findByOrderByDtPublishedDesc();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findAllByCategory(Long id) {
        return articleRepository.findAllByCategory(id, Sort.by("dtPublished").descending());
    }

    public List<Article> findAllByUsername(String username) {
        return articleRepository.findAllByUsername(username, Sort.by("dtCreated").descending());
    }

    @Transactional
    public void updateArticlePublicFieldsFromDto(ArticleDto articleDto) {
        Article article = findById(articleDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Статья '%s' не найдена",
                        articleDto.getTitle())));

        //Если нет изменений - выходим
        if (article.getTitle().equals(articleDto.getTitle())
                && article.getText().equals(articleDto.getText())
                && article.getStatus().equals(articleDto.getStatus()))
            return;

        article.setText(articleDto.getText());
        article.setTitle(articleDto.getTitle());
        article.setStatus(articleDto.getStatus());
    }


    @Transactional
    public void createArticleFromDto(ArticleDto articleDto) {
        Article article = new Article();
        article.setUser(userService.findByUsername("bob").orElseThrow());//здесь надо как-то получать авторизованного юзера
        article.setDtCreated(LocalDateTime.now());
        article.setTitle(articleDto.getTitle());
        article.setText(articleDto.getText());
        article.setStatus(articleDto.getStatus());
        articleRepository.save(article);
    }

    public List<Article> findAllByStatus(String status) {
        return articleRepository.findAllByStatusName(status);
    }

    @Transactional
    public void updateStatus(Long articleId, String statusName) {
        Optional<Article> article = articleRepository.findById(articleId);
        Optional<Status> status = statusService.findByName(statusName);
        if (article.isPresent() && status.isPresent()) {
            articleRepository.save(
                    article.map(art -> {
                        art.setStatus(status.get());
                        art.setDtPublished(LocalDateTime.now());
                        return art;
                    }).get());
        }
    }
}
