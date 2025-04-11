package ru.edme.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.edme.model.UserAccess;
import ru.edme.repository.UserAccessRepository;

/**
 * Сервис аутентификации и регистрации пользователей.
 * <p>
 * Обрабатывает:
 * <ul>
 *     <li>Регистрацию новых пользователей ({@link #register(RegisterRequest)})</li>
 *     <li>Аутентификацию существующих пользователей ({@link #authenticate(AuthRequest)})</li>
 * </ul>
 * Использует {@link AuthenticationManager} для проверки логина и пароля,
 * {@link JwtService} — для генерации JWT-токенов, и {@link PasswordEncoder} — для шифрования паролей.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserAccessRepository userAccessRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя в системе.
     * <p>
     * Создаёт нового пользователя, сохраняет его в БД и возвращает JWT-токен.
     *
     * @param request объект запроса на регистрацию с полями: имя, логин, пароль, роль
     * @return объект ответа {@link AuthResponse} с JWT-токеном
     */
    public AuthResponse register(RegisterRequest request) {
        UserAccess user = new UserAccess();
        user.setFullName(request.getFullName());
        user.setUserLogin(request.getLogin());
        user.setUserPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserRole(request.getRole());

        UserAccess userSave = userAccessRepository.save(user);

        String token = jwtService.generateToken(userSave);
        return new AuthResponse(token);
    }

    /**
     * Аутентифицирует пользователя по логину и паролю.
     * <p>
     * Использует {@link AuthenticationManager}, чтобы проверить логин и пароль через
     * встроенный {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     * Затем загружает пользователя из базы и генерирует JWT-токен.
     *
     * @param request объект запроса с логином и паролем
     * @return объект {@link AuthResponse} с JWT-токеном
     * @throws UsernameNotFoundException если пользователь не найден по логину
     */
    public AuthResponse authenticate(AuthRequest request) {
        // Проверка пароля происходит внутри DaoAuthenticationProvider который вызывается authenticationManager-ом
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUserLogin(), request.getPassword()));

        UserAccess user = userAccessRepository.findByUserLogin(request.getUserLogin())// TODO: 06.04.2025 Наверное как-то можно удалить!
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
