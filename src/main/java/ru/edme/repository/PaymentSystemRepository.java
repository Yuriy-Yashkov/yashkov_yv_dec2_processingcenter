package ru.edme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.PaymentSystem;

@Repository
public interface PaymentSystemRepository extends JpaRepository<PaymentSystem, Long> {

}
