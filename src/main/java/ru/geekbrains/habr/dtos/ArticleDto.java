package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.entities.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String text;
    private User user;
    private Status status;
    private LocalDateTime dtCreated;
    private LocalDateTime dtPublished;
    private Long likesTotal;
    private Long commentsTotal;
}
