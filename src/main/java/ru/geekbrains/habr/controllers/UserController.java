package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.UserConverter;
import ru.geekbrains.habr.dtos.UserBannedDto;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.UserService;
import ru.geekbrains.habr.services.enums.ErrorMessage;
import ru.geekbrains.habr.validations.UserInfoValidator;

/**
 * Контроллер обработки запросов к пользовательским данным
 *
 * @author Миронова Ирина
 * @author Рожко Алексей
 *
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final UserInfoValidator userInfoValidator;

    /**
     * Возвращает пользователя по имени
     *
     * @param username Имя пользователя
     * @return DTO пользователя
     */
    @GetMapping("/{username}")
    public UserDto findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(), username))
                );

        return userConverter.entityToDto(user);
    }

    /**
     * Обновляет пользовательские данные
     *
     * @param userDto DTO пользователя
     */
    @PutMapping("/update")
    public void updateUserInfo(@RequestBody UserDto userDto) {
        userInfoValidator.validate(userDto);
        userService.updateUserInfoFromDto(userDto);
    }

    /**
     * Блокировка пользователя
     *
     * @param userBannedDto the user banned dto
     */
    @PostMapping("/moderation/ban")
    public void banUser(@RequestBody UserBannedDto userBannedDto) {
        userService.banUser(userBannedDto);

    }
}
