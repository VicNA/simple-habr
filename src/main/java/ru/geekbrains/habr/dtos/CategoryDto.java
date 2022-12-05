package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String cyrillicName;
}
