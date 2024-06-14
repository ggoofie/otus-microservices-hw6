package ru.otus.GatewayApplication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.otus.GatewayApplication.dto.LoginDTO;
import ru.otus.GatewayApplication.dto.RegistrationDTO;
import ru.otus.GatewayApplication.dto.UpdateProfileDTO;
import ru.otus.GatewayApplication.exception.ApiGatewayException;
import ru.otus.GatewayApplication.jwt.JwtTokenProvider;
import ru.otus.GatewayApplication.model.Token;
import ru.otus.GatewayApplication.model.User;
import ru.otus.GatewayApplication.repository.TokenRepository;
import ru.otus.GatewayApplication.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = getUser(loginDTO.getUsername());
        revokeAllTokenByUser(user.getUsername());
        var token = jwtTokenProvider.generateToken(authentication);
        tokenRepository.save(Token.builder()
                .token(token)
                .isLoggedOut(false)
                .user(user)
                .build());
        return token;
    }

    @Override
    public void registration(RegistrationDTO registrationDTO) {
        if(userRepository.existsByUsername(registrationDTO.getUsername())){
            throw new ApiGatewayException(HttpStatus.BAD_REQUEST, "Пользователь уже существует");
        }
        User user = User.builder()
                .name(registrationDTO.getName())
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

    @Override
    public void revokeAllTokenByUser(String username) {
        User user = getUser(username);
        var tokens = user.getTokens();
        if (!tokens.isEmpty()) {
            tokens.forEach(t -> t.setLoggedOut(true));
        }
        tokenRepository.saveAll(tokens);
    }

    @Override
    public User getProfile(String username) {
        User user = getUser(username);
        userCheck(username);
        return user;
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    @Override
    public void setProfile(String username, UpdateProfileDTO updateProfileDTO) {
        userCheck(username);
        var user = getUser(username);
        user.setName(updateProfileDTO.getName());
        user.setEmail(updateProfileDTO.getEmail());
        userRepository.save(user);
    }

    private static void userCheck(String username) {
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            throw new ApiGatewayException(HttpStatus.FORBIDDEN, "Это чужой профиль");
        }
    }
}
