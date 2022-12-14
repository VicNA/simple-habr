package ru.geekbrains.habr.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.services.enums.Filter;

import java.util.List;

/**
 * Контроллер обработки запросов получения константных значений
 *
 * @author Николаев Виктор
 *
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/constants")
public class ConstantController {

    /**
     * Возвращает наименования сортировок
     *
     * @return Список значений сортировок
     */
    @GetMapping("/sortingNames")
    public List<String> getSortingNames() {
        return List.of(Filter.DT_PUBLISHED.getName(), Filter.RATING.getName());
    }
}
