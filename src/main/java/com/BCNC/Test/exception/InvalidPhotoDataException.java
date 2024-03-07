package com.BCNC.Test.exception;

public class InvalidPhotoDataException extends RuntimeException {
    public InvalidPhotoDataException(String message) {
        super(message);
    }
}