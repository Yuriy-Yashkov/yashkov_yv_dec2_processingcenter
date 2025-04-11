package ru.edme.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {// TODO: 21.03.2025 Не использую, пока

    private long id;
    private String accountNumber;
    private BigDecimal balance;
//    private Currency currency;
//    private IssuingBank issuingBank;
}
