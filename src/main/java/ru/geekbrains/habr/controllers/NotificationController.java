package ru.geekbrains.habr.controllers;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.NotificationConverter;
import ru.geekbrains.habr.dtos.NotificationDto;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.NotificationService;
import ru.geekbrains.habr.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;
    private final NotificationConverter notificationConverter;

    @GetMapping
    public List<NotificationDto> findAllByUser(@RequestParam(name = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден", username)));

        return notificationService.findAllByUser(user).stream()
                .map(notificationConverter::entityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotification(@RequestBody NotificationDto notificationDto){
        notificationService.createNotification(notificationDto);
    }
}
