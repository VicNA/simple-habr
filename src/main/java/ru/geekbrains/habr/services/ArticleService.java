package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final int SIZE_PAGE = 3;

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final StatusService statusService;

    /**
     * Получает страницу опубликованных статей, отсортированных
     * по дате публикации в обратном порядке (сначала последние)
     *
     * @return Страница статей
     * @author Миронова Ирина
     */
    public Page<Article> findAllSortDescPage(int page) {
        return articleRepository.findAllByStatusNamePage("published",
                PageRequest.of(page, SIZE_PAGE, Sort.by("dtPublished").descending()));
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    /**
     * Получает страницу статей, указанной категории
     *
     * @param id   - идентификатор категории
     * @param page - идентификатор страницы
     * @return Страница статей
     * @author Миронова Ирина
     */
    public Page<Article> findAllByCategoryPage(Long id, int page) {
        return articleRepository.findAllByCategoryPage("published", id,
                PageRequest.of(page, SIZE_PAGE, Sort.by("dtPublished").descending()));
    }


    /**
     * Получает страницу статей определенного пользователя
     *
     * @param username - ник пользователя
     * @param page     - идентификатор страницы
     * @return Страница статей определенного пользователя
     * @author Миронова Ирина
     */
    public Page<Article> findAllByUsernamePage(String username, int page) {
        return articleRepository.findAllByUsernamePage(username,
                PageRequest.of(page, SIZE_PAGE, Sort.by("dtCreated").descending()));
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

    /**
     * Получает страницу статей указанного статуса
     *
     * @param status имя статуса
     * @return Страницу статей
     * @author Миронова Ирина
     */
    public Page<Article> findAllByStatusPage(String status, int page) {
        return articleRepository.findAllByStatusNamePage(status, PageRequest.of(page, SIZE_PAGE));
    }

    /**
     * Обновляет статус статьи
     *
     * @param articleId  id статьи
     * @param statusName имя статуса
     * @author Николаев Виктор
     */
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

    /**
     * Удаляет статью
     *
     * @param article - статья
     * @author Миронова Ирина
     */
    @Transactional
    public void deleteArticle(Article article) {
        articleRepository.delete(article);
    }
}