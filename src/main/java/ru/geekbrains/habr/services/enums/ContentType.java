package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum ContentType {
    COMMENT("comment"),
    ARTICLE("article");
    private final String field;

    ContentType(String field) {
        this.field = field;
    }
}
