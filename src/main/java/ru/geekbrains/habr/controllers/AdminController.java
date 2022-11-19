package ru.geekbrains.habr.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.converters.UserConverter;
import ru.geekbrains.habr.dtos.ModeratorDto;
import ru.geekbrains.habr.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@AllArgsConstructor
public class AdminController {
    private final UserService userService;
    private final UserConverter userConverter;

    @GetMapping("/view/moderators")
    public List<ModeratorDto> getModerators() {
        return userService.findModerators().stream()
                .map(userConverter::toModeratorDto)
                .collect(Collectors.toList());
    }
}
