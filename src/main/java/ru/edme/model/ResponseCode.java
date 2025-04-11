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
public class ResponseCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String errorCode;
    private String errorDescription;
    private String error_level;
}
