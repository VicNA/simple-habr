package ru.geekbrains.habr.services.enums;

public enum ArticleStatus {
    HIDDEN, MODERATING, PUBLISHED;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
