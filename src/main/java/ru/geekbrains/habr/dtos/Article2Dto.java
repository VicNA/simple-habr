package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Article2Dto {
    private Long id;
    private String title;
    private String text;
    private String username;
    private String status;
    private String dtCreated;
}
