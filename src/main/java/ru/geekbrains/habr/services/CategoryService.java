package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Category;
import ru.geekbrains.habr.repositories.CategoryRepository;

import java.util.List;

/**
 * Сервис обработки категории
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Получает список категории
     *
     * @return Список
     */
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllByArticleId(Long articleId)  {
        return categoryRepository.findAllByArticleId(articleId);
    }
}
