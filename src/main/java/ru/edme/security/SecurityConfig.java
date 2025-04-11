package ru.edme.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности для Spring Security.
 * <p>
 * Отвечает за настройку цепочки фильтров безопасности, политик авторизации, а также компонентов
 * аутентификации (менеджера и кодировщика паролей).
 * <p>
 * Используется JWT для аутентификации и Stateless подход (без сессий).
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Фильтр для обработки JWT-токенов. Добавляется в цепочку фильтров до стандартного UsernamePasswordAuthenticationFilter.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Основная конфигурация Spring Security.
     *
     * <ul>
     *     <li>Отключает CSRF-защиту, так как REST API обычно не использует формы.</li>
     *     <li>Отключает сессии (используется Stateless — только токены).</li>
     *     <li>Настраивает разрешения:
     *         <ul>
     *             <li>Публичный доступ к Swagger-документации, аутентификации и техническим эндпоинтам.</li>
     *             <li>Все остальные запросы требуют аутентификации.</li>
     *         </ul>
     *     </li>
     *     <li>Добавляет кастомный JWT-фильтр перед стандартной аутентификацией по логину/паролю.</li>
     * </ul>
     *
     * @param http объект {@link HttpSecurity}, предоставляемый Spring Security
     * @return объект {@link SecurityFilterChain}, содержащий все настройки безопасности
     * @throws Exception в случае ошибок конфигурации
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)// В случае REST оно ненужно. Это там какое-то скрытое поле на jsp, для большей безопасности.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//  В REST нет сессий, отключаем.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(// Оставлять только общедоступные ресурсы
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml",
                                "/api/v1/auth/**",
                                "/v1/tables/**",
                                "/api/v1/test"
                        ).permitAll() // Публичные эндпоинты
                        .anyRequest().authenticated() // Остальное для всех аутентифицированных пользователей. Или все остальные запросы требуют авторизации.
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);// Добавляем наш фильтр перед UsernamePasswordAuthenticationFilter.class
        return http.build();
    }

    /**
     * Возвращает {@link AuthenticationManager}, который используется для выполнения аутентификации.
     * <p>
     * Получается из объекта {@link AuthenticationConfiguration}, предоставляемого Spring.
     *
     * @param authenticationConfiguration конфигурация аутентификации, содержащая готовый AuthenticationManager
     * @return настроенный {@link AuthenticationManager}
     * @throws Exception в случае ошибок получения AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Кодировщик паролей, используемый для хранения и проверки паролей пользователей.
     * <p>
     * Использует BCrypt — надёжный алгоритм хэширования.
     * <p>
     * Этот бин автоматически используется в {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     *
     * @return объект {@link PasswordEncoder}, реализующий BCrypt
     */
    @Bean
    // Пароли хранятся в таком виде. Автоматически используется в DaoAuthenticationProvider, который ходит в БД через UserDetailsService.
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
