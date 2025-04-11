package ru.edme.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.edme.model.Card;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    @EntityGraph(attributePaths = {"paymentSystem"})
    Optional<Card> findById(Long id);

    @EntityGraph(attributePaths = {"paymentSystem"})
    List<Card> findAll();
}
