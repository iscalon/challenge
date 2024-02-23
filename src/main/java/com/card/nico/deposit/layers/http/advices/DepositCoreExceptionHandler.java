package com.card.nico.deposit.layers.http.advices;

import com.card.nico.deposit.layers.core.exceptions.DepositCoreException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class DepositCoreExceptionHandler {

    @ExceptionHandler(DepositCoreException.class)
    public ResponseEntity<String> handleException(DepositCoreException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }
}
