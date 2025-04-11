package ru.edme.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO запроса")
public class AuthRequest {

    private String userLogin;
    private String password;
}
