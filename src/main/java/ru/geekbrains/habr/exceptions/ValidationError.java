package ru.geekbrains.habr.exceptions;

import lombok.Data;

import java.util.List;

@Data
public class ValidationError {
    private List<ValidationFieldError> fields;
    String message;

    public ValidationError(List<ValidationFieldError> fields, String message) {
        this.fields = fields;
        this.message = message;
    }
}
