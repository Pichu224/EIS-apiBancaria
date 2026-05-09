package com.unq.eis.apibancaria.exception;

public class EmailYaExistenteException extends RuntimeException {
    public EmailYaExistenteException(String message) {
        super(message);
    }
}
