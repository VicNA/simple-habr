package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.habr.dtos.JwtRequest;
import ru.geekbrains.habr.dtos.JwtResponse;
import ru.geekbrains.habr.dtos.NewUserDto;
import ru.geekbrains.habr.entities.User;
import ru.geekbrains.habr.exceptions.AppError;
import ru.geekbrains.habr.services.UserService;
import ru.geekbrains.habr.utils.JwtTokenUtil;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authorization")
    public ResponseEntity<?> authentication(@RequestBody JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (BadCredentialsException e) {

            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Некорректный логин или пароль"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody NewUserDto newUserDto) {

        if (!newUserDto.getPassword().equals(newUserDto.getConfirmPassword())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }

        if (userService.findByUsername(newUserDto.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с таким именем уже существует"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(newUserDto.getUsername());
        user.setRealname(newUserDto.getRealname());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        userService.createUser(user);
        UserDetails userDetails = userService.loadUserByUsername(newUserDto.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody String tokenRequest) {
        String tokenResponse = jwtTokenUtil.generateToken(tokenRequest);

        return ResponseEntity.ok(new JwtResponse(tokenResponse));
    }
}
