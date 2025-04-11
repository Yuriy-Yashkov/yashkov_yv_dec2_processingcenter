package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class TableService implements ru.edme.service.TableService {

    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;

    @Override
    public void deleteTables() {
        try {
            Resource resource = resourceLoader.getResource("classpath:db/changelog/ddl/drop_all_tables.sql");
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении SQL-скрипта", e);
        }
    }

    @Override
    public void clearTables() {
        try {
            Resource resource = resourceLoader.getResource("classpath:db/changelog/ddl/clear_tables.sql");
            String sql = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            jdbcTemplate.execute(sql);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении SQL-скрипта", e);
        }
    }
}
