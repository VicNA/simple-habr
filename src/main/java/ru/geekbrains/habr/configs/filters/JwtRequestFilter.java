package ru.geekbrains.habr.configs.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.geekbrains.habr.services.UserService;
import ru.geekbrains.habr.utils.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Фильтр Jwt токенов.
 * Проверка токена в headers http запроса на соответствие
 * и добавление в контекст данных о пользователе, если токен валидный.
 * Выполняется до {@code UsernamePasswordAuthenticationFilter}
 *
 * @author Рожко Алексей
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwt);
        }

        if ((username != null) && (SecurityContextHolder.getContext().getAuthentication() == null)) {
            UserDetails userDetails = userService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        }
        filterChain.doFilter(request, response);
    }

}
