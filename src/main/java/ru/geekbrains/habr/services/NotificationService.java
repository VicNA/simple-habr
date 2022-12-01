package ru.geekbrains.habr.services;

/*
 * Сервис для работы с уведомлениями пользователя
 *
 * @author Миронова Ирина
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.NotificationDto;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.NotificationRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    /**
     * Получает список уведомлений указанного пользователя
     *
     * @param user - пользователь
     * @return Список уведомлений
     */
    public List<Notification> findAllByUser(User user) {
        return notificationRepository.findAllByRecipient(user);
    }

    /**
     * Создание нового уведомления
     *
     * @param notificationDto - уведомление
     */
    @Transactional
    public void createNotification(NotificationDto notificationDto) {
        User recipient = userService.findByUsername(notificationDto.getRecipient())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Получатель '%s' не найден", notificationDto.getRecipient()))
                );

        User sender = userService.findByUsername(notificationDto.getSender())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Отправитель '%s' не найден", notificationDto.getSender()))
                );

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setText(notificationDto.getText());
        notificationRepository.save(notification);
    }

    /**
     * Возвращает количество уведомлений указанного пользователя
     *
     * @param user - пользователь
     * @return Количество уведомлений
     */
    public int countNotification(User user) {
        return findAllByUser(user).size();
    }


    /**
     * Удаление всех уведомлений указанного пользователя
     *
     * @param user - пользователь
     */
    @Transactional
    public void deleteNotificationsByUser(User user) {
        notificationRepository.deleteAllByRecipient(user);
    }
}
