package ru.otus.GatewayApplication.service;

import ru.otus.GatewayApplication.dto.LoginDTO;
import ru.otus.GatewayApplication.dto.RegistrationDTO;
import ru.otus.GatewayApplication.dto.UpdateProfileDTO;
import ru.otus.GatewayApplication.model.User;

public interface AuthService {
    String login(LoginDTO loginDTO);
    void registration(RegistrationDTO registrationDTO);
    void revokeAllTokenByUser(String username);
    User getProfile(String username);
    void setProfile(String username, UpdateProfileDTO updateProfileDTO);
}
