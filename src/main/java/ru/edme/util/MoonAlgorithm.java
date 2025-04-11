package ru.edme.util;

import java.util.Random;

public class MoonAlgorithm {

    // Проверка номера с алгоритмом Луна
    public static boolean isValidMoon(String number) {
        int sum = 0;
        boolean doubleDigit = false;

        // Проходим по цифрам справа налево
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit; // Меняем флаг удвоения
        }

        return sum % 10 == 0; // Если сумма делится на 10 — номер валиден
    }

    // Генерация номера с корректной контрольной цифрой (без префикса)
    public static String generateMoonNumber(int length) {
        if (length < 2) {
            throw new IllegalArgumentException("Длина номера должна быть не меньше 2");
        }

        Random random = new Random();
        StringBuilder number = new StringBuilder();

        // Генерируем случайные цифры (кроме последней контрольной)
        for (int i = 0; i < length - 1; i++) {
            number.append(random.nextInt(10));
        }

        // Вычисляем контрольную цифру
        int checkDigit = calculateMoonCheckDigit(number.toString());
        number.append(checkDigit); // Добавляем контрольную цифру в конец

        return number.toString();
    }

    // Вычисление контрольной цифры для алгоритма Луна
    private static int calculateMoonCheckDigit(String number) {
        int sum = 0;
        boolean doubleDigit = true;

        // Идём справа налево, начиная с предпоследней цифры (контрольная ещё не добавлена)
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            doubleDigit = !doubleDigit;
        }

        // Контрольная цифра — число, которое сделает сумму кратной 10
        return (10 - (sum % 10)) % 10;
    }
}
