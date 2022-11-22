package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Объект для фронта
 *
 * @author Николаев Виктор
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class ModeratorDto {
    private String username;
    private String realname;
    private String description;
}
