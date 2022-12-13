package ru.geekbrains.habr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.habr.entities.Article;
import ru.geekbrains.habr.entities.Like;
import ru.geekbrains.habr.entities.Notification;
import ru.geekbrains.habr.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByRecipient(User user);

    void deleteAllByRecipient(User user);

    Optional<Notification> findBySenderAndText(User user, String text);
}
