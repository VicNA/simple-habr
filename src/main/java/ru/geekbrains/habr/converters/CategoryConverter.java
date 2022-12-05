package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.CategoryDto;
import ru.geekbrains.habr.entities.Category;

@Component
public class CategoryConverter {

    public CategoryDto entityToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getCyrillicName());
    }
}
