package com.tfg.exceptions;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Invalid credentials provided.");
    }
}
