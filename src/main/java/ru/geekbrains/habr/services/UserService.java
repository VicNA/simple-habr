package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void updateUserInfoFromDto(UserDto userDto) {
        User user = findByUsername(userDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден",
                        userDto.getUsername())));

        user.setUsername(userDto.getUsername());
        user.setRealname(userDto.getRealname());
        user.setDtBirth(userDto.getDtBirth());
        user.setDescription(userDto.getDescription());
    }
}
