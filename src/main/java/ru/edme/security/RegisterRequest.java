package ru.edme.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edme.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String login;
    private String fullName;
    private String password;
    private Role role;
}

