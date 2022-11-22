package ru.geekbrains.habr.validations;

public interface Validator<E> {
    void validate(E e);
}
