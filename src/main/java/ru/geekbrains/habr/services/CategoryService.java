package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Category;
import ru.geekbrains.habr.repositories.CategoryRepository;

import java.util.List;

/**
 * Сервис обработки категории
 *
 * @author Николаев Виктор
 * @author Татьяна Коваленко
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Находит все категории
     *
     * @return Список категории
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Находит категории конкретной статьи
     *
     * @param articleId Идентификатор статьи
     * @return Список категории
     */
    public List<Category> findAllByArticleId(Long articleId)  {
        return categoryRepository.findAllByArticleId(articleId);
    }
}
