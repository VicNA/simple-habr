package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;
    private LocalDate dtBirth;
    private String description;
}
