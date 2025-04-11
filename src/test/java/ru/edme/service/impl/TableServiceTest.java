package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.service.TableService;

import java.util.List;

@SpringBootTest
class TableServiceTest extends PostgreSQLContainerInitializer {

    @Autowired
    private TableService tableService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("Тест удаления всех таблиц")
    void deleteTables() {
        // Проверка, что таблицы существуют
        List<String> tablesBefore = getTableNames();
        Assertions.assertFalse(tablesBefore.isEmpty(), "Перед удалением должны быть таблицы!");

        // Удаляем таблицы
        tableService.deleteTables();

        // Проверяем, что таблицы удалены
        List<String> tablesAfter = getTableNames();
        Assertions.assertEquals(2, tablesAfter.size(), "После удаления таблиц не должно остаться!");
    }

    @Test
    @DisplayName("Тест очистки таблиц")
    void clearTables() {
        // Добавляем тестовые данные
        jdbcTemplate.execute("INSERT INTO card (card_number, expiration_date, holder_name, payment_system_id) " +
                "VALUES ('4123450000000019', '2025-12-31', 'IVAN I.IVANOV', 1)," +
                "('5123450000000024', '2025-12-31', 'SEMION E.PETROV', 2)");

        // Проверяем, что записи есть
        int countBefore = countRows("card");
        Assertions.assertTrue(countBefore > 0, "Перед очисткой в таблице должны быть записи!");

        // Очищаем таблицы
        tableService.clearTables();

        // Проверяем, что записи удалены, но таблицы остались
        int countAfter = countRows("card");
        Assertions.assertEquals(0, countAfter, "После очистки таблицы должны быть пустыми!");

        List<String> tablesAfter = getTableNames();
        Assertions.assertFalse(tablesAfter.isEmpty(), "После очистки таблицы должны остаться!");
    }

    // Метод для получения списка всех таблиц в БД
    private List<String> getTableNames() {
        return jdbcTemplate.queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'",
                String.class
        );
    }

    // Метод для подсчёта строк в таблице
    private int countRows(String tableName) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
    }
}