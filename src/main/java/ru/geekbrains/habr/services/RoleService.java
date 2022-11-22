package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Role;
import ru.geekbrains.habr.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final static String ROLE_USER = "ROLE_USER";
    public Role getUserRole() {
        return roleRepository.findByName(ROLE_USER).get();
    }

    public Optional<Role> findByName(String role) {
        return roleRepository.findByName(role);
    }

    public List<Role> findByNameIn(List<String> names) {
        return roleRepository.findByNameIn(names);
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
