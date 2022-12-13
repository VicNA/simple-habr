package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class NewCommentDto {

    private String text;
    private String username;
    private Long articleId;
    private Long parentCommentId;

}
