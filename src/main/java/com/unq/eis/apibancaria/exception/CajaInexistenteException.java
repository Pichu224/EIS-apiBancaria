package com.unq.eis.apibancaria.exception;

public class CajaInexistenteException extends RuntimeException {
    public CajaInexistenteException(String message) {
        super(message);
    }
}
