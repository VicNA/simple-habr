package ru.geekbrains.habr.services;

/*
 * Сервис для работы с уведомлениями пользователя
 *
 * @author Миронова Ирина
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.NotificationRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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
     * @param recipient - получатель уведомления
     * @param sender    - отправитель уведомления
     * @param text      - текст уведомления
     */
    @Transactional
    public void createNotification(String recipient, String sender, String text) {
        User newRecipient = userService.findByUsername(recipient)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Получатель '%s' не найден", recipient))
                );

        User newSender = userService.findByUsername(sender)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Отправитель '%s' не найден", sender))
                );

        Notification notification = new Notification();
        notification.setRecipient(newRecipient);
        notification.setSender(newSender);
        notification.setText(text);
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


    /**
     * Возвращает уведомление по указанному отправителю и тексту
     *
     * @param sender - отправитель уведомления
     * @param text   - техт уведомления
     * @return Количество уведомлений
     */
    public Optional<Notification> findBySenderAndText(User sender, String text) {
        return notificationRepository.findBySenderAndText(sender, text);
    }

    /**
     * Удаление уведомления
     *
     * @param notification - уведомление
     */
    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }
}
