package ru.edme.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для генерации и валидации JWT-токенов.
 * <p>
 * Используется для создания токенов с пользовательскими данными (включая роли) и их последующей проверки.
 */
@Service
public class JwtService {

    @Value("${token.key}")
    private String jwtSigningKey;

    @Value("${token.lifetime}")
    private Duration time;

    /**
     * Генерирует JWT-токен на основе предоставленного {@link UserDetails}.
     * <ul>
     *     <li>Включает в токен имя пользователя (subject).</li>
     *     <li>Добавляет роли пользователя как кастомное claim-поле {@code roles}.</li>
     *     <li>Устанавливает дату выпуска и срок действия токена.</li>
     * </ul>
     *
     * @param userDetails объект с данными пользователя
     * @return сгенерированный JWT-токен
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Преобразуем роли к String
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + time.toMillis()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Извлекает имя пользователя (subject) из токена.
     *
     * @param token JWT-токен
     * @return имя пользователя
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Проверяет валидность токена:
     * <ul>
     *     <li>Имя пользователя в токене должно совпадать с переданным пользователем</li>
     *     <li>Срок действия токена не должен быть истёкшим</li>
     * </ul>
     *
     * @param token       JWT-токен
     * @param userDetails пользователь, с которым сравниваются данные токена
     * @return {@code true}, если токен валиден
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {// Если совпадает имя и не просрочен - true
        return (extractUsername(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Проверяет, истёк ли срок действия токена.
     *
     * @param token JWT-токен
     * @return {@code true}, если токен истёк
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Извлекает список ролей из токена.
     * <p>
     * Роли сохраняются в claim-поле {@code roles} как {@code List<String>}.
     *
     * @param token JWT-токен
     * @return список ролей
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * Универсальный метод для извлечения claim-поля из токена.
     *
     * @param token          JWT-токен
     * @param claimsResolver функция, извлекающая нужное поле из {@link Claims}
     * @param <T>            тип извлекаемого значения
     * @return значение claim
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    /**
     * Извлекает все claim-поля из JWT-токена.
     * <p>
     * Также происходит валидация подписи токена.
     *
     * @param token JWT-токен
     * @return объект {@link Claims}, содержащий все поля токена
     */
    private Claims extractAllClaims(String token) { // Проверка токена.
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает ключ для подписи JWT, декодируя Base64-строку.
     *
     * @return объект {@link Key} для HMAC-подписи
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
