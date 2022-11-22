package ru.geekbrains.habr.converters;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.ModeratorDto;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;

import java.time.format.DateTimeFormatter;

/**
 * Конвертер сущности {@link User}
 *
 * @author Николаев Виктор
 * @version 1.0
 */
@Component
public class UserConverter {
    public UserDto entityToDto(User user) {
        return new UserDto(user.getUsername(),
                           user.getRealname(),
                           user.getDtBirth(),
                           user.getDescription(),
                           user.getRoles());
    }

    /**
     * Преобразует сущность пользоваетля в dto для фронта
     *
     * @param user Пользователь
     * @return Объект dto
     *
     * @author Николаев Виктор
     */
    public ModeratorDto toModeratorDto(User user) {
        return new ModeratorDto(
                user.getUsername(),
                user.getRealname(),
                user.getDescription());
    }
}
