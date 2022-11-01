package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
