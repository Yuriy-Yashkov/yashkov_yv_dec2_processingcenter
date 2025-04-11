package ru.edme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.model.PaymentSystem;
import ru.edme.repository.PaymentSystemRepository;
import ru.edme.service.PaymentSystemAllService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@PreAuthorize("hasAuthority('ADMIN')")
public class PaymentSystemSpringAllServiceImpl implements PaymentSystemAllService {

    private final PaymentSystemRepository paymentSystemRepository;
    private final Class<PaymentSystem> entityClass = PaymentSystem.class;

    @Override
    @Transactional
    @CachePut(value = "paymentSystem", key = "#result.id")
    public PaymentSystem save(PaymentSystem entity) {
        return paymentSystemRepository.save(entity);
    }

    @Override
    @Cacheable(value = "paymentSystem", key = "#id")
    public PaymentSystem findById(Long id) {
        log.info("Данные взяты из БД.");

        return paymentSystemRepository.findById(id).orElseThrow(
                () -> new RuntimeException(
                        String.format("Не удалось прочитать объект! - %s = %d", entityClass.getSimpleName(), id))
        );
    }

    @Override
    @Cacheable(value = "paymentSystems", key = "'all'")
    public List<PaymentSystem> findAll() {
        log.info("Данные взяты из БД.");

        return paymentSystemRepository.findAll();
    }

    @Override
    @Transactional
    @CachePut(value = "paymentSystem", key = "#entity.id")
    public PaymentSystem update(PaymentSystem entity) {
        PaymentSystem paymentSystem = findById(entity.getId());
        paymentSystem.setPaymentSystemName(entity.getPaymentSystemName());

        return paymentSystemRepository.save(paymentSystem);
    }

    @Override
    @Transactional
    @CacheEvict(value = "paymentSystem", key = "#id")
    public boolean delete(Long id) {
        PaymentSystem paymentSystem = findById(id);
        paymentSystemRepository.delete(paymentSystem);

        return true;
    }
}
