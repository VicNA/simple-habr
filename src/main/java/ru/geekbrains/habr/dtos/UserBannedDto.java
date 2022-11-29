package ru.geekbrains.habr.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserBannedDto {

    public String username;
    public int daysBan;

}
