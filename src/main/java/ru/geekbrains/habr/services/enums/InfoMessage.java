package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum InfoMessage {
    NOTIFICATION_COMMENT_INFO("Пользователь '%s' добавил комментарий к Вашей статье <'%s'>"),
    NOTIFICATION_LIKE_INFO("Пользователь '%s' поставил лайк Вашей статье <'%s'>"),
    NOTIFICATION_CALL_INFO("Пользователь %s призвал Вас %s <<%s>> c формулировкой: \"%s\""),

    USER_BAN_INFO("Пользователь '%s' забанен до %s"),

    COMMENT_BAN_INFO("Комментарий забанен");

    private final String field;

    InfoMessage(String field) {
        this.field = field;
    }
}
