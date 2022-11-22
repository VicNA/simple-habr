package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {
    private String username;
    private Long articleId;
}
