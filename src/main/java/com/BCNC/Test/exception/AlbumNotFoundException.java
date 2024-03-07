package com.BCNC.Test.exception;

import com.BCNC.Test.service.AlbumServiceImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientException;

import java.util.logging.ErrorManager;

@Getter
public class AlbumNotFoundException extends RuntimeException {

    private final RestClientException cause;

    public AlbumNotFoundException(String message, RestClientException cause) {
        super(message, cause);
        this.cause = cause;
    }
    public AlbumNotFoundException(String message) {
        super(message);
        this.cause = new RestClientException(message);
    }


}