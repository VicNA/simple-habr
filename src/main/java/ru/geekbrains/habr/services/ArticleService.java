package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Category;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.ArticleRepository;
import ru.geekbrains.habr.repositories.CategoryRepository;
import ru.geekbrains.habr.repositories.specifications.ArticleSpecifcation;
import ru.geekbrains.habr.services.enums.ArticleStatus;
import ru.geekbrains.habr.services.enums.ErrorMessage;
import ru.geekbrains.habr.services.enums.Filter;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Сервис обработки статей
 *
 * @author Николаев Виктор
 * @author Миронова Ирина
 * @author Татьяна Коваленко
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Value("${pagination.page-size}")
    private int SIZE_PAGE;

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    private final UserService userService;
    private final StatusService statusService;
    private final ImageService imageService;

    /**
     * Находит все опубликованные статьи указанного статуса
     * отсортированных по дате публикации
     *
     * @param page   Номер старницы
     * @param status Статус статей
     * @return Страница статей
     */
    public Page<Article> findAllPage(int page, ArticleStatus status) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(Filter.STATUS.getName(), status.toString())),
                PageRequest.of(page, SIZE_PAGE, Sort.by(Filter.DT_PUBLISHED.getName()).descending()));
    }

    /**
     * Находит статьи с определенным статусом и отфильтрованным по наименованию статьи
     * и указанной сортировкой
     *
     * @param page      Номер старницы
     * @param status    Статус статьи
     * @param titlePart Искомое слово в наименовании статьи
     * @param sort      Сортировка
     * @return Страницу статей
     */
    public Page<Article> findByFilter(int page, ArticleStatus status, String titlePart, Sort sort) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(
                        Filter.STATUS.getName(), status.toString(), Filter.TITLE.getName(), titlePart)),
                PageRequest.of(page, SIZE_PAGE, sort));
    }

    /**
     * Находит опубликованные статьи отсортированных по рейтингу
     *
     * @param size Количество записей
     * @return Страницу статей
     */
    public Page<Article> findByRating(int size) {
        return articleRepository.findAll(
                createSpecByFilters(Map.of(Filter.STATUS.getName(), ArticleStatus.PUBLISHED.toString())),
                PageRequest.of(0, size, Sort.Direction.DESC, Filter.RATING.getName())
        );
    }

    /**
     * Создает спецификацию по фильтру
     *
     * @param props Фильтры
     * @return Спецификацию
     */
    private Specification<Article> createSpecByFilters(Map<String, String> props) {
        Specification<Article> spec = Specification.where(null);

        if (props.isEmpty()) return spec;

        if (props.containsKey(Filter.STATUS.getName())) {
            spec = spec.and(ArticleSpecifcation.statusEquals(props.get(Filter.STATUS.getName())));
        }

        if (props.containsKey(Filter.TITLE.getName())) {
            spec = spec.and(ArticleSpecifcation.titleLike(props.get(Filter.TITLE.getName())));
        }

        return spec;
    }

    /**
     * Находит статью по идентификатору
     *
     * @param id Идентификатор
     * @return the optional
     */
    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    /**
     * Находит опубликованные статьи определенной категории
     *
     * @param id   Идентификатор категории
     * @param page Номер страницы
     * @param sort Сортировка
     * @return Страницу статей
     */
    public Page<Article> findAllByCategoryPage(Long id, int page, Sort sort) {
        return articleRepository.findAllByCategoryPage(ArticleStatus.PUBLISHED.toString(), id,
                PageRequest.of(page, SIZE_PAGE, sort));
    }

    /**
     * Находит статьи определенного пользователя
     *
     * @param username Имя пользователя
     * @param page     Номер страницы
     * @return Страницу статей
     */
    public Page<Article> findAllByUsernamePage(String username, int page) {
        return articleRepository.findAllByUsernamePage(username,
                PageRequest.of(page, SIZE_PAGE, Sort.by(Filter.DT_CREATED.getName()).descending()));
    }

    /**
     * Сохраняет изменения получаемой статьи в БД
     *
     * @param articleDto DTO статьи
     */
    @Transactional
    public void updateArticlePublicFieldsFromDto(ArticleDto articleDto) {
        Article article = findById(articleDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.ARTICLE_ID_ERROR.getField(), articleDto.getTitle()))
                );

        //Если нет изменений - выходим
        if (article.getTitle().equals(articleDto.getTitle())
                && article.getText().equals(articleDto.getText())
                && article.getStatus().equals(articleDto.getStatus())
                && article.getImagePath().equals(articleDto.getImagePath()))
            return;

        article.setText(articleDto.getText());
        article.setTitle(articleDto.getTitle());
        article.setStatus(articleDto.getStatus());

        if (article.getImagePath() != null && !article.getImagePath().equals(articleDto.getImagePath())) {
            imageService.deleteImage(article.getImagePath());
        }

        article.setImagePath(articleDto.getImagePath());
    }

    /**
     * Добавляет новую статью
     *
     * @param articleDto DTO статьи
     */
    @Transactional
    public Long createArticleFromDto(ArticleDto articleDto) {
        Article article = new Article();
        article.setUser(userService.findByUsername(articleDto.getAuthorUsername()).orElseThrow());
        article.setDtCreated(LocalDateTime.now());
        article.setTitle(articleDto.getTitle());
        article.setText(articleDto.getText());
        article.setStatus(articleDto.getStatus());
        article.setImagePath(articleDto.getImagePath());
        Article a = articleRepository.save(article);
        return a.getId();
    }

    /**
     * Получает статьи с определенным статусом
     *
     * @param status Имя статуса
     * @param page   Номер страницы
     * @return Страницу статей
     */
    public Page<Article> findAllByStatusPage(String status, int page) {
        return articleRepository.findAllByStatusNamePage(status, PageRequest.of(page, SIZE_PAGE));
    }

    /**
     * Сохраняет изменения статуса статьи
     *
     * @param articleId  Идентификатор статьи
     * @param statusName Наименование статуса
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
     * @param id Идентификатор статьи
     */
    @Transactional
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.ARTICLE_ID_ERROR.getField(), id)));

        articleRepository.delete(article);
    }

    /**
     * Меняет категории, к которым привязана статья, на категории с именами из списка
     *
     * @param articleId       Идентификатор статьи
     * @param categoriesNames Список категории
     */
    @Transactional
    public void updateCategories(Long articleId, List<String> categoriesNames) {
        Optional<Article> article = articleRepository.findById(articleId);

        if (article.isEmpty()) return;

        articleRepository.clearCategories(article.get().getId());

        Category category;
        for (String name : categoriesNames) {
            category = categoryRepository.findOneByName(name);
            articleRepository.addToCategory(article.get().getId(), category.getId());
            System.out.println(categoryRepository.findOneByName(name).getName());
        }
    }
}