package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModeratorDto {
    private String username;
    private String realname;
    private String dtBirth;
    private String description;
}
