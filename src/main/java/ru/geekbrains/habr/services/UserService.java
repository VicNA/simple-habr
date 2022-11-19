package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.dtos.UserDto;
import ru.geekbrains.habr.entities.Role;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.entities.enums.BaseRole;
import ru.geekbrains.habr.exceptions.ResourceNotFoundException;
import ru.geekbrains.habr.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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

    public void createUser(User user){
        user.setRoles(List.of(roleService.getUserRole()));
        userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) {
        User user = findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(String.format("Пользователь '%s' не найден",
                username)));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }

    public List<User> findModerators() {
        return userRepository.findAll(BaseRole.ROLE_MODERATOR.name());
    }

}
