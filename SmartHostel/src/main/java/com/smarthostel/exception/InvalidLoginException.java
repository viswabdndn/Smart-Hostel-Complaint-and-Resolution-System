package com.smarthostel.exception;

/*
 * Exactly the same as the original exception.InvalidLoginException.
 * Kept as a checked Exception so StudentService.login() throws it.
 */
public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }
}
