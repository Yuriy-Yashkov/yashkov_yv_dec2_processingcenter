package ru.edme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate transactionDate;
    private BigDecimal sum;
    private String transactionName;

    @ManyToOne
    private TransactionType transactionType;

    @ManyToOne
    private Card card;

    @ManyToOne
    private Terminal terminal;

    @ManyToOne
    private ResponseCode responseCode;
    private String authorizationCode;
    private LocalDateTime receivedFromIssuingBank;
    private LocalDateTime sentToIssuingBank;
}
