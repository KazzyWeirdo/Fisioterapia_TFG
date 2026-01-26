package com.tfg.exceptions;

public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
        super("The provided ID is invalid.");
    }
}
