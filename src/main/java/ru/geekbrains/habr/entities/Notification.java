package ru.geekbrains.habr.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import ru.geekbrains.habr.services.enums.ContentType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "text")
    private String text;

    @Column(name = "content_id")
    private Long contentId;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @CreationTimestamp
    @Column(name = "dt_created")
    private LocalDateTime dtCreated;
}
