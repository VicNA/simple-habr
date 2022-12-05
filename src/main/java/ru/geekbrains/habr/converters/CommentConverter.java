package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.dtos.CommentDto;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Comment;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class CommentConverter {

    public static CommentDto entityToDto(Comment comment){
           CommentDto dto =  new CommentDto().builder()
                .id(comment.getId())
                .text(comment.getText())
                .username(comment.getUser().getUsername())
                .date(comment.getDtCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")))
                .build();

                if(!comment.getComments().isEmpty()){
                    dto.setComments(comment.getComments().stream()
                               .map(CommentConverter::entityToDto).collect(Collectors.toList()));
                }

                return dto;
    }
}
