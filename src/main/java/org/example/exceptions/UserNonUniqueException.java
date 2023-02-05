package org.example.exceptions;

public class UserNonUniqueException extends Exception {
    public UserNonUniqueException(String message) {
        super(message);
    }
}
