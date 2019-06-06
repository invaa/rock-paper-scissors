package com.games.rps.api.controller;

import com.games.rps.api.dto.ErrorDto;
import com.games.rps.api.exception.GameIsNotActiveException;
import com.games.rps.api.exception.GameNotExistsException;
import com.games.rps.api.exception.MaxRoundsReachedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerConfiguration extends ResponseEntityExceptionHandler {

    private void logDebugIfEnabled(Exception exception) {
        if (log.isDebugEnabled()) {
            log.debug(exception.getMessage(), exception);
        }
    }

    private ResponseEntity<ErrorDto> handleException(Exception exception, HttpStatus status) {
        ErrorDto response = new ErrorDto(LocalDateTime.now(), status.value(), exception.getMessage());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(GameNotExistsException.class)
    public ResponseEntity<ErrorDto> handleGameNotExistsException(GameNotExistsException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxRoundsReachedException.class)
    public ResponseEntity<ErrorDto> handleMaxRoundsReachedException(MaxRoundsReachedException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(GameIsNotActiveException.class)
    public ResponseEntity<ErrorDto> handleGameIsNotActiveException(GameIsNotActiveException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDto> handleTypeMismatchExceptionException(MethodArgumentTypeMismatchException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorDto> handleBindException(BindException exception) {
        logDebugIfEnabled(exception);
        return handleException(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return handleException(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
