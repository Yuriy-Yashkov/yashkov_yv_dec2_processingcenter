package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.exception.EntityNotFoundException;
import ru.edme.model.Card;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.CardRepository;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.CardAllService;
import ru.edme.util.MoonAlgorithm;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardSpringAllServiceImpl implements CardAllService {

    private final CardRepository cardRepository;
    private final PaymentSystemRepository paymentSystemRepository;

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public Card save(Card entity) {
        if (!MoonAlgorithm.isValidMoon(entity.getCardNumber())) {
            log.warn("Некорректный номер карты!");
            return Card.builder().build();
        }
        // Сохраняем вложенные объекты
        PaymentSystem paymentSystem = paymentSystemRepository.save(entity.getPaymentSystem());

        entity.setPaymentSystem(paymentSystem);

        return cardRepository.save(entity);
    }

    @Override
    @Cacheable(value = "card", key = "#id")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Не удалось прочитать объект! - %s = %d", Card.class.getSimpleName(), id))
        );
    }

    @Override
    @Cacheable(value = "cards", key = "'all'")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    @Override
    @Transactional
    @CachePut(value = "card", key = "#result.id")
    public Card update(Card entity) {
        Card card = findById(entity.getId());
        card.setCardNumber(entity.getCardNumber());
        card.setExpirationDate(entity.getExpirationDate());
        card.setHolderName(entity.getHolderName());
        card.setPaymentSystem(entity.getPaymentSystem());

        return cardRepository.save(card);
    }

    @Override
    @Transactional
    @CacheEvict(value = "card", key = "#id")
    public boolean delete(Long id) {
        Card card = findById(id);
        cardRepository.delete(card);

        return true;
    }
}
