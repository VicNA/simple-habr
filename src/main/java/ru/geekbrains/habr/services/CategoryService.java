package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Category;
import ru.geekbrains.habr.repositories.CategoryRepository;

import java.util.List;

/**
 * Сервис для работы с категориями
 *
 * @author
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
