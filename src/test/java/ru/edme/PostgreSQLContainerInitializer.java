package ru.edme;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@Transactional // Откат после каждого теста
@ActiveProfiles("test")
public abstract class PostgreSQLContainerInitializer {

    private static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:16.0-alpine");

    @BeforeAll
    static void startContainer() {
        postgreSQLContainer.start();
    }

    /**
     * Регистрирует динамические свойства подключения к базе данных PostgreSQL,
     * используя значения из контейнера Testcontainers.
     * <p>
     * Эти свойства будут подставлены в Spring Boot контекст при запуске тестов,
     * заменяя стандартные значения из application.yml/properties.
     *
     * @param registry объект, предоставляемый Spring Boot для регистрации динамических конфигурационных свойств
     */
    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}
