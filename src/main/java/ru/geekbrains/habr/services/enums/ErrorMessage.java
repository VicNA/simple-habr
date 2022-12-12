package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    ARTICLE_ID_ERROR("Статья с id = '%d' не найдена"),

    USER_USERNAME_ERROR("Пользователь '%s' не найден"),
    USER_MIN_BIRTH_ERROR("Дата рождения не может быть ранее '%s'"),
    USER_MAX_BIRTH_ERROR("Дата рождения не может быть позднее текущей даты"),

    AUTHENTICATION_PASSWORD_ERROR("Пароли не совпадают"),
    AUTHENTICATION_INCORRECT_PASSWORD_ERROR("Некорректный логин или пароль"),
    AUTHENTICATION_INCORRECT_USERNAME_ERROR("Пользователь с таким именем уже существует"),

    COMMENT_ID_ERROR("Комментарий с id = '%d' не найден"),
    COMMENT_DELETE_ERROR("Комментарий удалён по какой-то причине"),

    FILE_LOAD_ERROR("Ошибка загрузки файла"),
    IMAGE_ERROR("Картинка отсутствует");

    private final String field;

    ErrorMessage(String field) {
        this.field = field;
    }
}
