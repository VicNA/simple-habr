package ru.geekbrains.habr.validations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.exceptions.ValidationException;
import ru.geekbrains.habr.exceptions.ValidationFieldError;
import ru.geekbrains.habr.services.enums.ErrorMessage;
import ru.geekbrains.habr.services.enums.Filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserInfoValidator implements Validator<UserDto> {

    @Value("${validation.birth-min-date}")
    private LocalDate birthMinDate;

    @Override
    public void validate(UserDto userDto) {
        List<ValidationFieldError> errorList = new ArrayList<>();
        String message = "";

        if (userDto.getDtBirth() == null) {
            return;
        }

        if (userDto.getDtBirth().isBefore(birthMinDate)) {
            message = String.format(ErrorMessage.USER_MIN_BIRTH_ERROR.getField(), birthMinDate.toString());
            errorList.add(new ValidationFieldError(Filter.DT_BIRTH.getName(), userDto.getDtBirth().toString(), message));
        }

        if (userDto.getDtBirth().isAfter(LocalDate.now())) {
            message = ErrorMessage.USER_MAX_BIRTH_ERROR.getField();
            errorList.add(new ValidationFieldError(Filter.DT_BIRTH.getName(), userDto.getDtBirth().toString(), message));
        }

        if (!errorList.isEmpty()) {
            throw new ValidationException(message, errorList);
        }

    }
}
