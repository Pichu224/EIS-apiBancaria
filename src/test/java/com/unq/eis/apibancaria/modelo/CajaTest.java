package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import com.unq.eis.apibancaria.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CajaTest {

    private Usuario usuario1;
    private Usuario usuario2;
    private Caja caja1;
    private Caja caja2;
    private Caja caja3;

    @BeforeEach
    void setUp(){
        usuario1 = new Usuario("nico@gmail.com","1234","Nicolas","Vaccaro","40.123.456");
        usuario2 = new Usuario("mati@gmail.com","4567","Matias","Alvarez","40.777.258");
        caja1 = new Caja(1L,"testCaja",usuario1);
    }

    @Test
    public void CreacionDeCaja(){

        caja2 = new Caja(2L,102L,"caja2.test2.api",usuario1);
        caja3 = new Caja(103L,"caja3.test.api",TipoCaja.CajaAhorro);

        assertEquals(1L, caja1.getNroCaja());
        assertEquals("testCaja", caja1.getAlias());
        assertEquals(BigDecimal.ZERO, caja1.getSaldo());
        assertEquals(TipoCaja.CajaAhorro, caja1.getTipoCaja());
        assertEquals(caja1.getUsuario().getNombre(), usuario1.getNombre());

        assertEquals(2L, caja2.getIdCaja());
        assertEquals(102L, caja2.getNroCaja());
        assertEquals("caja2.test2.api", caja2.getAlias());
        assertEquals(BigDecimal.ZERO, caja2.getSaldo());
        assertEquals(TipoCaja.CajaAhorro, caja2.getTipoCaja());
        assertEquals(caja2.getUsuario().getNombre(), usuario1.getNombre());

        assertEquals(103L, caja3.getNroCaja());
        assertEquals("caja3.test.api", caja3.getAlias());
        assertEquals(BigDecimal.ZERO, caja3.getSaldo());
        assertEquals(TipoCaja.CajaAhorro, caja3.getTipoCaja());
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

    @Test
    public void DepositarDineroExitoso(){

        assertEquals(BigDecimal.ZERO, caja1.getSaldo());

        caja1.depositar(BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), caja1.getSaldo());

    }

    @Test
    public void ErrorAlDepositarMontoIgualoMenorACero(){

        assertThrows(MontoInvalidoException.class, () -> {caja1.depositar(BigDecimal.ZERO);});
        assertThrows(MontoInvalidoException.class, () -> {caja1.depositar(BigDecimal.valueOf(-1));});
    }

    @Test
    public void RetirarDineroDeCajaExitoso(){

        caja1.depositar(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), caja1.getSaldo());

        caja1.retirar(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.ZERO, caja1.getSaldo());
    }

    @Test
    public void ErrorRetirarMontoIgualoMenorACero(){

        assertThrows(MontoInvalidoException.class, () -> {caja1.retirar(BigDecimal.ZERO);});
        assertThrows(MontoInvalidoException.class, () -> {caja1.retirar(BigDecimal.valueOf(-1));});
    }

    @Test
    public void ErrorRetirarMontoConCajaSinSaldo(){

        assertThrows(SaldoInsuficienteException.class, () -> {caja1.retirar(BigDecimal.ONE);});
    }
}
