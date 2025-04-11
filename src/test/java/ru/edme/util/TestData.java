package ru.edme.util;

import ru.edme.model.Card;
import ru.edme.model.PaymentSystem;

import java.time.LocalDate;

public class TestData {

    public PaymentSystem paymentSystem = PaymentSystem.builder()
            .paymentSystemName("Какая-то платёжная система")
            .build();
    public PaymentSystem paymentSystemId = PaymentSystem.builder()
            .id(3L)
            .paymentSystemName("Какая-то платёжная система")
            .build();

    public Card card = Card.builder()
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystem)
            .build();
    public Card cardId = Card.builder()
            .id(3L)
            .cardNumber(MoonAlgorithm.generateMoonNumber(16))
            .expirationDate(LocalDate.now())
            .holderName("Какое-то имя")
            .paymentSystem(paymentSystemId)
            .build();
}
