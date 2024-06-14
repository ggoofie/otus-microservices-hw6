package ru.otus.GatewayApplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.GatewayApplication.model.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
