package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.geekbrains.habr.entities.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private String username;
    private String realname;
    private LocalDate dtBirth;
    private String description;
    private List<Role> roles;
}
