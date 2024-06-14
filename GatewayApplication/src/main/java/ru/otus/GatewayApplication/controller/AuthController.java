package ru.otus.GatewayApplication.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.otus.GatewayApplication.dto.LoginDTO;
import ru.otus.GatewayApplication.dto.RegistrationDTO;
import ru.otus.GatewayApplication.dto.UpdateProfileDTO;
import ru.otus.GatewayApplication.model.User;
import ru.otus.GatewayApplication.service.AuthService;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public String registerUser(@RequestBody RegistrationDTO registrationDTO) {
        authService.registration(registrationDTO);
        return "Вы успешно зарегистрировались!";
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody LoginDTO loginDTO) {
        return Collections.singletonMap("token", authService.login(loginDTO));
    }

    @GetMapping("/profile/{username}")
    public User getProfile(@PathVariable("username") String username) {
        return authService.getProfile(username);
    }

    @PutMapping("/profile/{username}")
    public void setProfile(@PathVariable("username") String username, @RequestBody UpdateProfileDTO updateProfileDTO) {
        authService.setProfile(username, updateProfileDTO);
    }

    @GetMapping("/logout")
    public void logout() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        authService.revokeAllTokenByUser(username);
        SecurityContextHolder.clearContext();
    }
}
