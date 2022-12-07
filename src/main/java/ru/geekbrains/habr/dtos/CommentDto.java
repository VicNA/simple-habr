package ru.geekbrains.habr.dtos;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Comment;
import ru.geekbrains.habr.entities.User;

import javax.persistence.*;
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
