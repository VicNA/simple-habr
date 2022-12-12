package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum InfoMessage {
    NOTIFICATION_COMMENT_INFO("Пользователь '%s' добавил комментарий к Вашей статье <'%s'>"),
    NOTIFICATION_LIKE_INFO("Пользователь '%s' поставил лайк Вашей статье <'%s'>"),

    USER_BAN_INFO("Пользователь '%s' забанен до %s");

    private final String field;

    InfoMessage(String field) {
        this.field = field;
    }
}
