package com.tfg.application.exceptions;

public class InvalidPageOrSizeException extends RuntimeException {
    public InvalidPageOrSizeException() {
        super("Invalid page or size.");
    }
}
