package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.ArticleRepository;
import ru.geekbrains.habr.repositories.specifications.ArticleSpecifcation;
import ru.geekbrains.habr.services.enums.ArticleStatus;
import ru.geekbrains.habr.services.enums.Filter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final int SIZE_PAGE = 3;

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final StatusService statusService;

    private final ImageService imageService;

    /**
     * Получает страницу опубликованных статей с указанной сортировкой
     *
     * @param page   Номер старницы
     * @param status Статус статей
     * @return Страница статей
     */
    public Page<Article> findAllPage(int page, ArticleStatus status) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(Filter.STATUS.getField(), status.toString())),
                PageRequest.of(page, SIZE_PAGE, Sort.by(Filter.DT_PUBLISHED.getField()).descending()));
    }

    /**
     * Получает страницу опубликованных статей с указанной сортировкой
     * по искомому слову в загаловке статей
     *
     * @param page      Номер старницы
     * @param status    Статус статей
     * @param titlePart Искомое слово
     * @param sort      Сортировка
     * @return Страница статей
     */
    public Page<Article> findAllPage(int page, ArticleStatus status, String titlePart, Sort sort) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(
                        Filter.STATUS.getField(), status.toString(), Filter.TITLE.getField(), titlePart)),
                PageRequest.of(page, SIZE_PAGE, sort));
    }

    public Page<Article> findByRating(int size) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(Filter.STATUS.getField(), ArticleStatus.PUBLISHED.toString())),
                PageRequest.of(0, size, Sort.Direction.DESC, Filter.RATING.getField())
        );
    }

    private Specification<Article> createSpecByFilters(Map<String, String> props) {
        Specification<Article> spec = Specification.where(null);

        if (props.isEmpty()) return spec;

        if (props.containsKey(Filter.STATUS.getField())) {
            spec = spec.and(ArticleSpecifcation.statusEquals(props.get(Filter.STATUS.getField())));
        }

        if (props.containsKey(Filter.TITLE.getField())) {
            spec = spec.and(ArticleSpecifcation.titleLike(props.get(Filter.TITLE.getField())));
        }

        return spec;
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
    public Page<Article> findAllByCategoryPage(Long id, int page, Sort sort) {
        return articleRepository.findAllByCategoryPage("published", id,
                PageRequest.of(page, SIZE_PAGE, sort));
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
                && article.getStatus().equals(articleDto.getStatus())
                && article.getImagePath().equals(articleDto.getImagePath()))
            return;

        article.setText(articleDto.getText());
        article.setTitle(articleDto.getTitle());
        article.setStatus(articleDto.getStatus());

        if(article.getImagePath()!=null && !article.getImagePath().equals(articleDto.getImagePath())){
            imageService.deleteImage(article.getImagePath());
        }
        article.setImagePath(articleDto.getImagePath());
    }


    @Transactional
    public void createArticleFromDto(ArticleDto articleDto) {
        Article article = new Article();
        article.setUser(userService.findByUsername(articleDto.getAuthorUsername()).orElseThrow());
        article.setDtCreated(LocalDateTime.now());
        article.setTitle(articleDto.getTitle());
        article.setText(articleDto.getText());
        article.setStatus(articleDto.getStatus());
        article.setImagePath(articleDto.getImagePath());
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