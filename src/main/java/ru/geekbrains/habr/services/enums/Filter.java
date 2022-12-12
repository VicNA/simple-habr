package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum Filter {
    STATUS("status"),
    TITLE("title"),
    DT_PUBLISHED("dtPublished"),
    RATING("articleRating.rating"),

    DT_BIRTH("dtBirth");

    private final String field;

    Filter(String field) {
        this.field = field;
    }
}
