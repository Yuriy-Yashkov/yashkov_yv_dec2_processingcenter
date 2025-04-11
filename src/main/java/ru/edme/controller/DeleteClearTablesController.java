package ru.edme.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.edme.service.TableService;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/tables")
public class DeleteClearTablesController {

    private final TableService tableService;

    @Operation(description = "Удаление всех таблиц")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteTables() {
        tableService.deleteTables();
        return ResponseEntity.ok("Все таблицы удалены.");
    }

    @Operation(description = "Очистка всех таблиц")
    @PostMapping("/clear")
    public ResponseEntity<String> clearTables() {
        tableService.clearTables();
        return ResponseEntity.ok("Все таблицы очищены.");
    }
}
