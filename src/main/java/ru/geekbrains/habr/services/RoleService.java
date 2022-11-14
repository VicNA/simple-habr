package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Role;
import ru.geekbrains.habr.repositories.RoleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    private final static String ROLE_USER = "ROLE_USER";
    public Role getUserRole() {
        return roleRepository.findByName(ROLE_USER).get();
    }
}
