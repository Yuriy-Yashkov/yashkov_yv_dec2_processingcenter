package ru.edme.service.cache;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.impl.PaymentSystemSpringAllServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
public class PaymentSystemCacheTest {

    @Autowired
    private PaymentSystemSpringAllServiceImpl paymentSystemService;

    @MockBean
    private PaymentSystemRepository paymentSystemRepository;

    @Test
    void testFindByIdShouldUseCache() {
        PaymentSystem testEntity = new PaymentSystem(10L, "VISA");

        // 1-й вызов — должен пойти в репозиторий
        Mockito.when(paymentSystemRepository.findById(10L))
                .thenReturn(Optional.of(testEntity));

        PaymentSystem firstCall = paymentSystemService.findById(10L);
        assertEquals("VISA", firstCall.getPaymentSystemName());

        // 2-й вызов — должен взять из кэша, findById НЕ должен вызываться
        PaymentSystem secondCall = paymentSystemService.findById(10L);

        Mockito.verify(paymentSystemRepository, times(1)).findById(10L);
    }

    @Test
    void testSaveShouldUpdateCache() {
        PaymentSystem saved = new PaymentSystem(20L, "Mastercard");
        Mockito.when(paymentSystemRepository.save(Mockito.any()))
                .thenReturn(saved);

        PaymentSystem result = paymentSystemService.save(saved);

        assertEquals("Mastercard", result.getPaymentSystemName());

        // Теперь должен быть кэширован
        PaymentSystem cached = paymentSystemService.findById(20L);
        Mockito.verify(paymentSystemRepository, never()).findById(20L);
    }

    @Test
    void testDeleteShouldEvictCache() {
        PaymentSystem entity = new PaymentSystem(30L, "JCB");

        Mockito.when(paymentSystemRepository.findById(30L))
                .thenReturn(Optional.of(entity));

        boolean deleted = paymentSystemService.delete(30L);
        assertTrue(deleted);

        // После удаления вызов findById должен снова обратиться в репозиторий
        paymentSystemService.findById(30L);
        Mockito.verify(paymentSystemRepository, times(2)).findById(30L);
    }
}
