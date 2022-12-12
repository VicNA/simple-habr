package ru.geekbrains.habr.dtos;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

    private Long id;
    private String text;
    private String username;
    private String date;
    private List<CommentDto> comments;
    private boolean banned;
}
