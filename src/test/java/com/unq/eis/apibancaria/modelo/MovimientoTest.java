package com.unq.eis.apibancaria.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovimientoTest {

    private Usuario usuario;
    private Caja caja;
    private Movimiento movimiento;

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
}
