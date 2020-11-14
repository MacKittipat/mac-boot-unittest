package com.mackittipat.boot.unittest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CurrencyConverterException extends Exception {

    public CurrencyConverterException(String message) {
        super(message);
    }
}
