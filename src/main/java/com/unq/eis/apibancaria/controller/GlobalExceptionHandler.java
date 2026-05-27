package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler { // Hay que modificar el HttpStatus para que muestre el codigo que corresponda.

    @ExceptionHandler(MailInvalidoException.class)
    public ResponseEntity<?> handlerMailInvalidoException(MailInvalidoException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ContraseniaVaciaException.class)
    public ResponseEntity<?> handlerContraseniaVaciaException(ContraseniaVaciaException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailYaExistenteException.class)
    public ResponseEntity<?> handlerEmailYaExistenteException(EmailYaExistenteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsuarioInexistenteException.class)
    public ResponseEntity<?> handlerUsuarioInexistenteException(UsuarioInexistenteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IdNuloException.class)
    public ResponseEntity<?> handlerIdNuloException(IdNuloException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NroCajaYaExistenteException.class)
    public ResponseEntity<?> handlerNroCajaYaExistenteException(NroCajaYaExistenteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AliasYaExistenteException.class)
    public ResponseEntity<?> handlerAliasYaExistenteException(AliasYaExistenteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CajaInexistenteException.class)
    public ResponseEntity<?> handlerCajaInexistenteException(CajaInexistenteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MontoInvalidoException.class)
    public ResponseEntity<?> handlerMontoInvalidoException(MontoInvalidoException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<?> handlerSaldoInsuficienteException(SaldoInsuficienteException e, WebRequest request){
        ErrorResponse errorDetails = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
