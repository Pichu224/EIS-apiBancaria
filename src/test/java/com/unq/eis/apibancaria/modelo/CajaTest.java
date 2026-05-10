package com.unq.eis.apibancaria.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CajaTest {

    private Usuario usuario1;
    private Usuario usuario2;
    private Caja caja1;
    private Caja caja2;

    @BeforeEach
    void setUp(){
        usuario1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        caja1 = new Caja(1L,"testCaja",usuario1);
    }

    @Test
    public void CreacionDeCaja(){

        assertEquals(caja1.getNroCaja(), 1L);
        assertEquals(caja1.getAlias(), "testCaja");
        assertEquals(caja1.getSaldo(), BigDecimal.ZERO);
        assertEquals(caja1.getTipoCaja(), TipoCaja.CajaAhorro);
        assertEquals(caja1.getUsuario().getNombre(), usuario1.getNombre());
    }

    @Test
    public void CambiarLosValoresDeUnaCaja(){

        usuario2 = new Usuario("mati@gmail.com","456","Matias","Alvarez","40.777.258");
        caja1.setNroCaja(2L);
        caja1.setAlias("CajaTest");
        caja1.setTipoCaja(TipoCaja.CajaCorriente);
        caja1.setSaldo(BigDecimal.ONE);
        caja1.setUsuario(usuario2);

        assertEquals(caja1.getNroCaja(),2L);
        assertEquals(caja1.getAlias(), "CajaTest");
        assertEquals(caja1.getSaldo(), BigDecimal.ONE);
        assertEquals(caja1.getTipoCaja(), TipoCaja.CajaCorriente);
        assertEquals(caja1.getUsuario().getNombre(), usuario2.getNombre());

    }
}
