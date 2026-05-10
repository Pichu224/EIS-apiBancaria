package com.unq.eis.apibancaria.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CajaTest {

    private Usuario usuario1;
    private Usuario usuario2;
    private Caja caja1;

    @BeforeEach
    void setUp(){
        usuario1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuario2 = new Usuario("mati@gmail.com","456","Matias","Alvarez","40.777.258");
        caja1 = new Caja(1L,"testCaja",usuario1);
    }

    @Test
    public void CreacionDeCaja(){

        assertEquals(1L, caja1.getNroCaja());
        assertEquals("testCaja", caja1.getAlias());
        assertEquals(BigDecimal.ZERO, caja1.getSaldo());
        assertEquals(TipoCaja.CajaAhorro, caja1.getTipoCaja());
        assertEquals(caja1.getUsuario().getNombre(), usuario1.getNombre());
    }

    @Test
    public void CambiarLosValoresDeUnaCaja(){

        caja1.setNroCaja(2L);
        caja1.setAlias("CajaTest");
        caja1.setTipoCaja(TipoCaja.CajaCorriente);
        caja1.setSaldo(BigDecimal.ONE);
        caja1.setUsuario(usuario2);

        assertEquals(2L, caja1.getNroCaja());
        assertEquals("CajaTest", caja1.getAlias());
        assertEquals(BigDecimal.ONE, caja1.getSaldo());
        assertEquals(TipoCaja.CajaCorriente, caja1.getTipoCaja());
        assertEquals(caja1.getUsuario().getNombre(), usuario2.getNombre());
    }
}
