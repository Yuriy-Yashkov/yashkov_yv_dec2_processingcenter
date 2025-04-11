package ru.edme.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcquiringBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String bic;
    private String abbreviatedName;
}
