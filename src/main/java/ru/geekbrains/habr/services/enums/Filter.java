package ru.geekbrains.habr.services.enums;

import lombok.Getter;

@Getter
public enum Filter {
    STATUS("status"),
    TITLE("title"),
    DT_PUBLISHED("dtPublished"),
    DT_CREATED("dtCreated"),
    RATING("articleRating.rating"),
    DT_BIRTH("dtBirth");

    private final String name;

    Filter(String name) {
        this.name = name;
    }
}
