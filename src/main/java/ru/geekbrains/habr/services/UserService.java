package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.UserBannedDto;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.Role;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.services.enums.UserRole;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.UserRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

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

    public void createUser(User user) {
        user.setRoles(List.of(roleService.getUserRole()));
        userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден", username)));

        if(user.getDateBan()!=null && user.getDateBan().isAfter(LocalDateTime.now())){
            String dateBan = user.getDateBan().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm"));

            throw new ResourceNotFoundException(String.format("Пользователь '%s' забанен до %s", username,dateBan));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    /**
     * Возвращает список пользователей с определенной ролью
     *
     * @param role Роль пользователя
     * @return Список пользователей
     */
    public List<User> findAllByRole(UserRole role) {
        return userRepository.findAll(role.name());
    }

    /**
     * Обновляет список ролей у пользователя
     *
     * @param username Имя пользователя
     * @param userRole Присваемая роль
     */
    @Transactional
    public void updateUserRole(String username, UserRole userRole) {
        userRepository.findByUsername(username).ifPresent(user -> {

            if (userRole.getRoles().size() == user.getRoles().size()) return;

            List<Role> roles = new ArrayList<>();

            switch (userRole) {
                case ROLE_USER:
                    roleService.findByName(userRole.name()).ifPresent(roles::add);
                    break;
                case ROLE_MODERATOR:
                    roles.addAll(roleService.findByNameIn(
                            List.of(UserRole.ROLE_USER.name(), UserRole.ROLE_MODERATOR.name())));
                    break;
                case ROLE_ADMIN:
                    roles.addAll(roleService.findAll());
                    break;
            }

            user.setRoles(roles);
            userRepository.save(user);
        });
    }

    public void banUser(UserBannedDto userBannedDto) {
        User user = findByUsername(userBannedDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден",
                        userBannedDto.getUsername())));

        user.setDateBan(LocalDateTime.now().plusDays(userBannedDto.daysBan));

        userRepository.save(user);
    }
}
