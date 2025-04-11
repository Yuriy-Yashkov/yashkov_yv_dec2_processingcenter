package ru.edme.service.cache;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import ru.edme.model.Card;
import ru.edme.repository.CardRepository;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("test")
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
public class CardCacheTest {

    @Autowired
    private CardAllService cardAllService;

    @MockBean
    private CardRepository cardRepository;

    @Test
    void testFindByIdShouldUseCache() {
        Card testEntity = new TestData().cardId;

        // 1-й вызов — должен пойти в репозиторий
        Mockito.when(cardRepository.findById(testEntity.getId()))
                .thenReturn(Optional.of(testEntity));

        cardAllService.findById(testEntity.getId());

        // 2-й вызов — должен взять из кэша, findById НЕ должен вызываться
        cardAllService.findById(testEntity.getId());

        Mockito.verify(cardRepository, times(1)).findById(testEntity.getId());
    }

    @Test
    void testSaveShouldUpdateCache() {
        Card saved = new TestData().cardId;
        saved.setId(saved.getId() + 1);

        Mockito.when(cardRepository.save(Mockito.any()))
                .thenReturn(saved);

        cardAllService.save(saved);

        // Теперь должен быть кэширован
        cardAllService.findById(saved.getId());
        Mockito.verify(cardRepository, never()).findById(saved.getId());
    }

    @Test
    void testDeleteShouldEvictCache() {
        Card entity = new TestData().cardId;
        entity.setId(entity.getId() + 2);

        Mockito.when(cardRepository.findById(entity.getId()))
                .thenReturn(Optional.of(entity));

        boolean deleted = cardAllService.delete(entity.getId());
        assertTrue(deleted);

        // После удаления вызов findById должен снова обратиться в репозиторий
        cardAllService.findById(entity.getId());
        Mockito.verify(cardRepository, times(2)).findById(entity.getId());
    }
}
