package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import ru.edme.model.PaymentSystem;
import ru.edme.service.PaymentSystemAllService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("v1/cards/payment-system")
@Tag(name = "Payment System Controller", description = "Управление платежными системами")
public class PaymentSystemController {

    private final PaymentSystemAllService paymentSystemAllService;

    @Operation(summary = "Создать платежную систему", description = "Добавляет новую платежную систему и возвращает её")
    @ApiResponse(responseCode = "201", description = "Платежная система успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentSystem.class)))
    @PostMapping
    public ResponseEntity<PaymentSystem> create(@RequestBody PaymentSystem entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentSystemAllService.save(entity));
    }

    @Operation(summary = "Получить платежную систему по ID",
            description = "Возвращает платежную систему по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Платежная система найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentSystem.class)))
    @ApiResponse(responseCode = "404", description = "Платежная система не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentSystem> findById(@Parameter(description = "ID платежной системы", example = "1")
//                                                  @Positive
                                                  @PathVariable("id") Long id) {
        return ResponseEntity.ok(paymentSystemAllService.findById(id));
    }

    @Operation(summary = "Получить список всех платежных систем", description = "Возвращает список всех платежных систем")
    @ApiResponse(responseCode = "200", description = "Список платежных систем",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentSystem.class)))
    @GetMapping
    public ResponseEntity<List<PaymentSystem>> findAll() {
        return ResponseEntity.ok(paymentSystemAllService.findAll());
    }

    @Operation(summary = "Обновить платежную систему", description = "Обновляет существующую платежную систему")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentSystem.class)))
    @PutMapping
    public ResponseEntity<PaymentSystem> update(@RequestBody PaymentSystem entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(paymentSystemAllService.update(entity));
    }

    @Operation(summary = "Удалить платежную систему", description = "Удаляет платежную систему по её ID")
    @ApiResponse(responseCode = "200", description = "Платежная система успешно удалена")
    @ApiResponse(responseCode = "404", description = "Платежная система не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID платежной системы", example = "1")
//                                       @Positive
                                       @PathVariable("id") Long id) {
        boolean delete = paymentSystemAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
