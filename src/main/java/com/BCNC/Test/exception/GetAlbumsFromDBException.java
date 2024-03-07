package com.BCNC.Test.exception;

public class GetAlbumsFromDBException extends RuntimeException {
    public GetAlbumsFromDBException(String message, Throwable cause) {
        super(message, cause);
    }
}