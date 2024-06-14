package ru.otus.GatewayApplication.dto;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String name;
    private String email;
}
