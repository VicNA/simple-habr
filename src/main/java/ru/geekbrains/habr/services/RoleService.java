package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Role;
import ru.geekbrains.habr.repositories.RoleRepository;
import ru.geekbrains.habr.services.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с ролями
 *
 * @author Николаев Виктор
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Получение роли пользователя
     *
     * @return роль
     */
    public Role getUserRole() {
        return roleRepository.findByName(UserRole.ROLE_USER.name()).get();
    }

    /**
     * Получение списка ролей пользователя
     *
     * @param role - название роли
     * @return список ролей
     */
    public Optional<Role> findByName(String role) {
        return roleRepository.findByName(role);
    }

    /**
     * Получение списка ролей пользователя
     *
     * @param names - список имен
     * @return список ролей
     */
    public List<Role> findByNameIn(List<String> names) {
        return roleRepository.findByNameIn(names);
    }

    /**
     * Получение списка ролей пользователя
     *
     * @return список ролей
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
