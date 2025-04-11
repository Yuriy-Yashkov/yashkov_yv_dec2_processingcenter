package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.model.Card;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class CardSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private CardAllService cardAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        Card card = testData.card;
        long expected = 1;

        Card cardSaved = cardAllService.save(card);
        long actual = cardSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        Card actual = cardAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> cardAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<Card> cardes = cardAllService.findAll();
        int actual = cardes.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        Card actual = cardAllService.findById(1L);
        actual.setHolderName("ZZZZZZZZZZZ");

        cardAllService.update(actual);
        Card expected = cardAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = cardAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        cardAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> cardAllService.findById(1L));
    }
}