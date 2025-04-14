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
import ru.edme.model.Card;
import ru.edme.service.CardAllService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cards")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Card Controller", description = "Управление картами (Card)")
public class CardController {

    private final CardAllService cardAllService;

    @Operation(summary = "Создать новую карту", description = "Создаёт новую карту и возвращает её данные")
    @ApiResponse(responseCode = "201", description = "Карта успешно создана",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Card.class)))
    @PostMapping
    public ResponseEntity<Card> create(@RequestBody Card entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cardAllService.save(entity));
    }

    @Operation(summary = "Получить карту по ID", description = "Возвращает карту по её уникальному идентификатору")
    @ApiResponse(responseCode = "200", description = "Карта найдена",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Card.class)))
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @GetMapping("/{id}")
    public ResponseEntity<Card> findById(@Positive @PathVariable("id") Long id) {
        return ResponseEntity.ok(cardAllService.findById(id));
    }

    @Operation(summary = "Получить список всех карт", description = "Возвращает все карты из базы данных")
    @ApiResponse(responseCode = "200", description = "Список карт",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Card.class)))
    @GetMapping
    public ResponseEntity<List<Card>> findAll() {
        return ResponseEntity.ok(cardAllService.findAll());
    }

    @Operation(summary = "Обновить данные карты", description = "Обновляет существующую карту")
    @ApiResponse(responseCode = "426", description = "Обновление требует дополнительных действий",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Card.class)))
    @PutMapping
    public ResponseEntity<Card> update(@RequestBody Card entity) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(cardAllService.update(entity));
    }

    @Operation(summary = "Удалить карту", description = "Удаляет карту по её ID")
    @ApiResponse(responseCode = "200", description = "Карта успешно удалена")
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID карты", example = "1")
                                       @Positive
                                       @PathVariable("id") Long id) {
        boolean delete = cardAllService.delete(id);

        return delete ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
