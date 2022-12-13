package ru.geekbrains.habr.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.services.enums.Filter;

import java.util.List;

@RestController
@RequestMapping("/api/v1/constants")
public class ConstantController {

    @GetMapping("/publishedDate")
    public List<String> getPublishedDate() {
        return List.of(Filter.DT_PUBLISHED.getName(), Filter.RATING.getName());
    }
}
