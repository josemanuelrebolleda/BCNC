package com.BCNC.Test.exception;

public class InvalidAlbumDataException extends RuntimeException {
    public InvalidAlbumDataException(String message) {
        super(message);
    }
}