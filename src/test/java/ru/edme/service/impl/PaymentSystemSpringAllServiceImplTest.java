package ru.edme.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import ru.edme.PostgreSQLContainerInitializer;
import ru.edme.model.PaymentSystem;
import ru.edme.service.PaymentSystemAllService;
import ru.edme.util.TestData;

import java.util.List;

@SpringBootTest
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class PaymentSystemSpringAllServiceImplTest extends PostgreSQLContainerInitializer {

    @Autowired
    private PaymentSystemAllService paymentSystemAllService;

    @Test
    void saveShouldCreateObjectTest() {
        TestData testData = new TestData();
        PaymentSystem paymentSystem = testData.paymentSystem;
        long expected = 1;

        PaymentSystem paymentSystemSaved = paymentSystemAllService.save(paymentSystem);
        long actual = paymentSystemSaved.getId();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void findByIdShouldReturnObjectTest() {
        PaymentSystem actual = paymentSystemAllService.findById(1L);

        Assertions.assertNotNull(actual);
    }

    @Test
    void findByIdShouldReturnEmptyObjectTest() {
        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemAllService.findById(0L));
    }

    @Test
    void findAllShouldReturnListSize() {
        int expected = 1;
        List<PaymentSystem> paymentSystems = paymentSystemAllService.findAll();
        int actual = paymentSystems.size();

        Assertions.assertTrue(expected <= actual);
    }

    @Test
    void updateTest() {
        PaymentSystem actual = paymentSystemAllService.findById(1L);
        actual.setPaymentSystemName("ZZZZZZZZZZZ");

        paymentSystemAllService.update(actual);
        PaymentSystem expected = paymentSystemAllService.findById(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnTrueTest() {
        boolean actual = paymentSystemAllService.delete(1L);
        boolean expected = true;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteShouldReturnRuntimeExceptionTest() {
        paymentSystemAllService.delete(1L);

        Assertions.assertThrows(RuntimeException.class, () -> paymentSystemAllService.findById(1L));
    }
}