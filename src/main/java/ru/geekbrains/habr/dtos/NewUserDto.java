package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.Date;

@Getter
@AllArgsConstructor
public class NewUserDto {
    private String username;

    private String password;

    private String confirmPassword;

    private String realname;
}
