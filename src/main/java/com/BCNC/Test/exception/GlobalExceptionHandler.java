package com.BCNC.Test.exception;

import com.BCNC.Test.entity.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AlbumNotFoundException.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<String> handleAlbumNotFoundException(AlbumNotFoundException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(GetAlbumsFromDBException.class)
    public ResponseEntity<String> handleGetAlbumsFromDBException(GetAlbumsFromDBException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(LoadPhotosException.class)
    public ResponseEntity<String> handleLoadPhotosException(LoadPhotosException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidPhotoDataException.class)
    public ResponseEntity<String> handleInvalidPhotoDataException(InvalidPhotoDataException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidAlbumDataException.class)
    public ResponseEntity<String> handleInvalidAlbumDataException(InvalidAlbumDataException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EnrichingAlbumsException.class)
    public ResponseEntity<String> handleEnrichingAlbumsException(EnrichingAlbumsException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EnrichAlbumsException.class)
    public ResponseEntity<String> handleEnrichAlbumsException(EnrichAlbumsException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(EnrichAndSaveAlbumsException.class)
    public ResponseEntity<String> handleEnrichAndSaveAlbumsException(EnrichAndSaveAlbumsException e) {
        logger.error(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
