package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
@Tag(name = "Test-controller", description = "Проверка работы аутентификации и авторизации")
public class TestController {

    @GetMapping("/public")
    @Operation(summary = "Публичный эндпоинт, доступен без аутентификации.")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Этот эндпоинт доступен всем!");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Доступно только для пользователей с ролью USER.")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("Этот эндпоинт доступен только USER!");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Доступно только для пользователей с ролью ADMINISTRATOR.")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("Этот эндпоинт доступен только ADMINISTRATOR!");
    }

    @GetMapping("/secured")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Доступно для всех аутентифицированных пользователей.")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("Этот эндпоинт доступен всем аутентифицированным пользователям!");
    }
}
