package ru.otus.GatewayApplication.exception;

import org.springframework.http.HttpStatus;

public class ApiGatewayException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public ApiGatewayException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiGatewayException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
