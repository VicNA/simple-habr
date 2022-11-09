package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.UserConverter;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.services.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping("/{username}")
    public UserDto findByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return userConverter.entityToDto(user);
    }


}
