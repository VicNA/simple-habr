package ru.geekbrains.habr.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import java.util.*;
import java.util.stream.Collectors;

/**
 * Утилита для обработки Jwt токенов
 *
 * @author Рожко Алексей
 * @version 1.0
 */
@Component
public class JwtTokenUtil {
    /**
     * Хранится в кодировке base64
     */
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Integer jwtLifetime;

    /**
     * Генерация нового токена.
     *
     * @param userDetails Основная информация о пользователе
     * @return Токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }


    /**
     * Получение username из токена.
     *
     * @param token Токен
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * Получение основной информации, хранящейся в токене.
     *
     * @param token Токен
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey((Decoders.BASE64.decode(secret)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверка токена на соответствие.
     *
     * @param token       Токен
     * @param userDetails Основная информация о пользователе
     * @return true - если токен валидный, false - если нет.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);

        if (username.isEmpty() || !username.equals(userDetails.getUsername())) {

            return false;
        }
        Date date = getClaimsFromToken(token).getExpiration();

        if (date == null || date.before(new Date())) {

            return false;
        }

        return true;
    }

}
