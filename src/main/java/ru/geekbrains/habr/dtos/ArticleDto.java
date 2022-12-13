package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.geekbrains.habr.entities.Status;

@Data
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private String text;
    private String imagePath;
    private String authorUsername;
    private Status status;
    private String dtCreated;
    private String dtPublished;
    private Long likesTotal;
    private Long commentsTotal;
}
