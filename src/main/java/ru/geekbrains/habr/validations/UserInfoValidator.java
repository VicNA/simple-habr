package ru.geekbrains.habr.validations;

import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.exceptions.ValidationException;
import ru.geekbrains.habr.exceptions.ValidationFieldError;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserInfoValidator implements Validator<UserDto> {
    @Override
    public void validate(UserDto userDto) {
        List<ValidationFieldError> errorList = new ArrayList<>();
        String message = "";

        if (userDto.getDtBirth() == null) {
            return;
        }

        if (userDto.getDtBirth().isBefore(LocalDate.of(1952, 1, 1))) {
            message = "Дата рождения не может быть ранее 01.01.1952";
            errorList.add(new ValidationFieldError("dtBirth", userDto.getDtBirth().toString(), message));
        }

        if (userDto.getDtBirth().isAfter(LocalDate.now())) {
            message = "Дата рождения не может быть позднее текущей даты";
            errorList.add(new ValidationFieldError("dtBirth", userDto.getDtBirth().toString(), message));
        }

        if (!errorList.isEmpty()) {
            throw new ValidationException(message, errorList);
        }

    }
}
