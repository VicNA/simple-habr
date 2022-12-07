package ru.geekbrains.habr.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.UserConverter;
import ru.geekbrains.habr.dtos.ModeratorDto;
import ru.geekbrains.habr.services.enums.UserRole;
import ru.geekbrains.habr.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер REST API личного кабинета админа
 *
 * @author Николаев Виктор
 * @version 1.0
 */

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserConverter userConverter;

    /**
     * Возвращает список пользователей с ролью {@code ROLE_MODERATOR}
     *
     * @return Список dto модераторов
     */
    @GetMapping("/view/moderators")
    public List<ModeratorDto> getModerators() {
        return userService.findAllByRole(UserRole.ROLE_MODERATOR).stream()
                .map(userConverter::toModeratorDto)
                .collect(Collectors.toList());
    }

    /**
     * Изменяет роли пользователя
     *
     * @param username Имя пользователя
     * @param role Присваемая роль
     */
    @PutMapping("/update/role")
    public void updateRoleModerator(
            @RequestParam("username") String username, @RequestParam(value = "role") String role) {
        userService.updateUserRole(username, UserRole.valueOf(role.toUpperCase()));
    }
}
