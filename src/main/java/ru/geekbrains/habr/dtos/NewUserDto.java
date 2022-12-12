package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewUserDto {
    private String username;

    private String password;

    private String confirmPassword;

    private String realname;
}
