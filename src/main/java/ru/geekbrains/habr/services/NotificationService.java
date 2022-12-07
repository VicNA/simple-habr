package ru.geekbrains.habr.services;

/**
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
import ru.geekbrains.habr.services.enums.ContentType;
import ru.geekbrains.habr.services.enums.UserRole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * @param recipient   - получатель уведомления
     * @param sender      - отправитель уведомления
     * @param text        - текст уведомления
     * @param contentId   - id комментария/статьи
     * @param contentType - тип контента (comment/article)
     */
    @Transactional
    public void createNotification(String recipient, String sender, String text, Long contentId, String contentType) {
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
        notification.setContentId(contentId);
        notification.setContentType(ContentType.valueOf(contentType.toUpperCase()));
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

    /**
     * Создание уведомлений группе пользовтелей с одной ролью
     *
     * @param userRole  - роль пользователей
     * @param sender    - отправитель уведомления
     * @param text      - текст уведомления
     */
    public void createNotificationForSpecificRole(UserRole userRole, String sender, String text,
                                                  Long contentId, String contentType) {
        List<String> usernameModerators = userService.findAllByRole(userRole)
                .stream().map(User::getUsername).collect(Collectors.toList());
        for (String usernameModerator : usernameModerators) {
            createNotification(usernameModerator, sender, text, contentId, contentType);

        }
    }
}
