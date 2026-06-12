package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovimientoTest {

    private Usuario usuario;
    private Caja caja;
    private Movimiento movimiento;
    private Movimiento movimiento1;
    private Movimiento movimiento2;

    @BeforeEach
    void setUp(){
        usuario = new Usuario("nico@gmail.com","1234","Nicolas","Vaccaro","40.123.456");
        caja = new Caja(1L,"testCaja.api",usuario);
    }

    @Test
    public void CreacionMovimientoExitosa(){
        movimiento = new Movimiento(1L,caja, BigDecimal.ONE,"2");

        assertEquals(1L, movimiento.getNroTransferencia());
        assertEquals(caja, movimiento.getCajaUtilizada());
        assertEquals(BigDecimal.ONE, movimiento.getMonto());
        assertEquals("Transferencia desde caja 1 hacia caja 2", movimiento.getDescripcion());
    }
    @Test
    public void ErrorCrearMovimientoMontoInvalido(){

        assertThrows(MontoInvalidoException.class, ()->{movimiento1 = new Movimiento(1L,caja, BigDecimal.ZERO,"2");});
        assertThrows(MontoInvalidoException.class, ()->{movimiento2 = new Movimiento(1L,caja, BigDecimal.valueOf(-1L),"2");});
    }
}
