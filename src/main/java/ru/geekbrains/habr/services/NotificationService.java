package ru.geekbrains.habr.services;

/**
 * Сервис для работы с уведомлениями пользователя
 *
 * @author Миронова Ирина
 */

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Comment;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.NotificationRepository;
import ru.geekbrains.habr.services.enums.ContentType;
import ru.geekbrains.habr.services.enums.UserRole;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
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
        if(recipient.equals(sender)){
            return;
        }
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

    /**
     * Создание уведомлений для нового комментария
     *
     * @param comment  - комментарий
     */
    public void sendAllNotification(Comment comment) {

        String textNotif = String.format("Пользователь %s добавил комментарий к Вашей статье <<%s>>",
                comment.getUser().getUsername(), comment.getArticle().getTitle());
        createNotification(
                comment.getArticle().getUser().getUsername(), comment.getUser().getUsername(), textNotif,
                comment.getArticle().getId(), ContentType.ARTICLE.getField());

        String nameRole = "@moderator ";

        if (comment.getText().toLowerCase().contains(nameRole)) {

            String whereName = "к статье";
            String whereId = comment.getArticle().getId().toString();
            String message = comment.getText().replace(nameRole, "");
            Long contentId = comment.getArticle().getId();
            String contentType = ContentType.ARTICLE.getField();

            if (comment.getParentComment() != null) {
                whereName = "к комментарию";
                whereId = comment.getParentComment().getId().toString();
                contentId = comment.getParentComment().getId();
                contentType = ContentType.COMMENT.getField();
            }

            String textNotifForModer = String.format("Пользователь %s призвал Вас %s <<%s>> c формулировкой: \"%s\"",
                    comment.getUser().getUsername(), whereName, whereId, message);
            createNotificationForSpecificRole(
                    UserRole.ROLE_MODERATOR, comment.getUser().getUsername(), textNotifForModer, contentId, contentType);
        }
    }


}
