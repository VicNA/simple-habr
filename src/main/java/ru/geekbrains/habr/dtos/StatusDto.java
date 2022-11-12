package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDto {
    private Long id;
    private String name;
}
