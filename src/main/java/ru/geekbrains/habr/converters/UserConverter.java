package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.ModeratorDto;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;

import java.time.format.DateTimeFormatter;

@Component
public class UserConverter {
    public UserDto entityToDto(User user) {
        return new UserDto(user.getUsername(),
                           user.getRealname(),
                           user.getDtBirth(),
                           user.getDescription(),
                           user.getRoles());
    }

    public ModeratorDto toModeratorDto(User user) {
        return new ModeratorDto(
                user.getUsername(),
                user.getRealname(),
                user.getDtBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                user.getDescription());
    }
}
