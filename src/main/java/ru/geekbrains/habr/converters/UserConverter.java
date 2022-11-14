package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;

@Component
public class UserConverter {
    public UserDto entityToDto(User user) {
        return new UserDto(user.getUsername(),
                           user.getRealname(),
                           user.getDtBirth(),
                           user.getDescription(),
                           user.getRoles());
    }
}
