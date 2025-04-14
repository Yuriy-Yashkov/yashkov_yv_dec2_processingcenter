package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.model.UserAccess;
import ru.edme.service.impl.UserAccessService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Tag(name = "User Controller", description = "Управление пользователями (Users)")
public class UserAccessController {

    private final UserAccessService userAccessService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @Operation(summary = "Создать нового пользователя", description = "Создаёт нового пользователя и возвращает его")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно создан",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAccess.class)))
    @PostMapping
    public ResponseEntity<UserAccess> create(@RequestBody UserAccess entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userAccessService.save(entity));
    }

    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('USER') and #id == authentication.principal.id)")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Пользователь найден",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAccess.class)))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @GetMapping("/{id}")
    public ResponseEntity<UserAccess> findById(@Positive @PathVariable("id") Long id) {
        return ResponseEntity.ok(userAccessService.findById(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Получить список всех пользователей", description = "Возвращает всех пользователей из базы данных")
    @ApiResponse(responseCode = "200", description = "Список пользователей",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAccess.class)))
    @GetMapping
    public ResponseEntity<List<UserAccess>> findAll() {
        return ResponseEntity.ok(userAccessService.findAll());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Обновить данные пользователя", description = "Обновляет существующего пользователя")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserAccess.class)))
    @PutMapping
    public ResponseEntity<UserAccess> update(@RequestBody UserAccess entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(userAccessService.update(entity));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно удалён")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID пользователя", example = "1")
                                       @Positive
                                       @PathVariable("id") Long id) {
        boolean delete = userAccessService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
