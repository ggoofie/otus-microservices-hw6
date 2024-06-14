package ru.otus.GatewayApplication.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    private String name;
    private String username;
    private String email;
    private String password;
}
