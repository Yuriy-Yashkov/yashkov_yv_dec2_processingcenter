package ru.edme.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр аутентификации на основе JWT.
 * <p>
 * Наследуется от {@link OncePerRequestFilter}, что гарантирует выполнение фильтра только один раз за запрос.
 * <p>
 * Обрабатывает каждый входящий HTTP-запрос, извлекая JWT-токен из заголовка "Authorization",
 * и, если токен действителен, устанавливает соответствующего пользователя в {@link SecurityContextHolder}.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Основная логика фильтрации запроса.
     * <p>
     * Если в заголовке {@code Authorization} присутствует Bearer-токен,
     * и он валиден, устанавливается аутентификация в {@link SecurityContextHolder}.
     *
     * @param request     входящий HTTP-запрос
     * @param response    HTTP-ответ
     * @param filterChain цепочка фильтров
     * @throws ServletException в случае ошибки сервлета
     * @throws IOException      в случае ошибок ввода/вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Получаем заголовок Authorization
        String authHeader = request.getHeader("Authorization");

        // Если заголовок отсутствует или не начинается с Bearer — пропускаем дальше
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Извлекаем JWT (без префикса Bearer)
        String jwt = authHeader.substring(7);

        // Получаем имя пользователя из токена
        String username = jwtService.extractUsername(jwt);

        // Проверяем, что имя пользователя найдено и ещё нет аутентификации в контексте безопасности
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Проверяем валидность токена
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("Токен корректен!");

                // Создаём объект аутентификации
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // Добавляем информацию о запросе (например, IP-адрес, сессию)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Устанавливаем аутентификацию в контекст безопасности
                SecurityContextHolder.getContext().setAuthentication(authToken); // Сохраняем пользователя в контекст безопасности, теперь spring знает о нас.
                log.info("Authorities: {}", userDetails.getAuthorities());

            }
        }
        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}
