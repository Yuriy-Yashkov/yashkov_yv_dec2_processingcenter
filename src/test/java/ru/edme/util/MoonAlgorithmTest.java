package ru.edme.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoonAlgorithmTest {

    @Test
    void testValidMoonNumber() {
        assertTrue(MoonAlgorithm.isValidMoon("4532015112830366")); // валидный номер
    }

    @Test
    void testInvalidMoonNumber() {
        assertFalse(MoonAlgorithm.isValidMoon("1234567812345678")); // невалидный номер
    }

    @Test
    void testGenerateValidMoonNumber() {
        for (int i = 2; i <= 20; i++) { // Проверяем номера разной длины
            String generated = MoonAlgorithm.generateMoonNumber(i);
            assertTrue(MoonAlgorithm.isValidMoon(generated), "Сгенерированный номер должен быть валидным: " + generated);
        }
    }

    @Test
    void testGenerateMoonNumberLength() {
        assertEquals(16, MoonAlgorithm.generateMoonNumber(16).length());
    }

    @Test
    void testGenerateMoonNumberInvalidLength() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            MoonAlgorithm.generateMoonNumber(1);
        });
        assertEquals("Длина номера должна быть не меньше 2", exception.getMessage());
    }
}