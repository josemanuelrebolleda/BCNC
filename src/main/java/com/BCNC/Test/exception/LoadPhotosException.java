package com.BCNC.Test.exception;

public class LoadPhotosException extends RuntimeException {
    public LoadPhotosException(String message, Throwable cause) {
        super(message, cause);
    }
}