package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;
    private String realname;
    private LocalDate dtBirth;
    private String description;
}
