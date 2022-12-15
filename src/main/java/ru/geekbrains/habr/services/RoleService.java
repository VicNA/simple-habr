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
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    /**
     * Находит роль по наименованию
     *
     * @param role Наименование роли
     * @return Роль
     */
    public Optional<Role> findByName(String role) {
        return roleRepository.findByName(role);
    }

    /**
     * Находит роли по списку наименований
     *
     * @param names Список наименований ролей
     * @return Список ролей
     */
    public List<Role> findByNameIn(List<String> names) {
        return roleRepository.findByNameIn(names);
    }

    /**
     * Находит все имеющиеся роли
     *
     * @return Список ролей
     */
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
