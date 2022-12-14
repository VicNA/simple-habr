package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.converters.NotificationConverter;
import ru.geekbrains.habr.dtos.NotificationDto;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.services.NotificationService;
import ru.geekbrains.habr.services.UserService;
import ru.geekbrains.habr.services.enums.ErrorMessage;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер обработки запросов к системе уведомлений
 *
 * @author Миронова Ирина
 *
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    private final NotificationConverter notificationConverter;

    /**
     * Получает список уведомлений указанного пользователя
     *
     * @param username - имя пользователя
     * @return Список уведомлений
     */
    @GetMapping
    public List<NotificationDto> findAllByUser(@RequestParam(name = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(), username))
                );

        return notificationService.findAllByUser(user).stream()
                .map(notificationConverter::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Создание нового уведомления
     *
     * @param notifDto - уведомление
     */
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNotification(@RequestBody NotificationDto notifDto) {
        notificationService.createNotification(notifDto.getRecipient(), notifDto.getSender(), notifDto.getText(),
                notifDto.getContentId(), notifDto.getContentType());
    }

    /**
     * Возвращает количество уведомлений указанного пользователя
     *
     * @param username - имя пользователя
     * @return Количество уведомлений
     */
    @GetMapping("/count")
    public int countNotification(@RequestParam(name = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(), username))
                );

        return notificationService.countNotification(user);
    }

    /**
     * Удаление всех уведомлений указанного пользователя
     *
     * @param username - имя пользователя
     */
    @DeleteMapping
    public void deleteNotificationsByUsername(@RequestParam(name = "username") String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(ErrorMessage.USER_USERNAME_ERROR.getField(), username))
                );

        notificationService.deleteNotificationsByUser(user);
    }
}
