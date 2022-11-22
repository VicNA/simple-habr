package ru.geekbrains.habr.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ValidationException extends RuntimeException {
    private String message;
    private List<ValidationFieldError> fields;

    public ValidationException(String message, List<ValidationFieldError> fields) {
        this.message = message;
        this.fields = fields;
    }
}
