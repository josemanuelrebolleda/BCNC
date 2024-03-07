package com.BCNC.Test.exception;

public class EnrichingAlbumsException extends RuntimeException {
    public EnrichingAlbumsException(String message, Throwable cause) {
        super(message, cause);
    }
}