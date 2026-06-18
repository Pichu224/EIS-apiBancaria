package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.CajasIgualesException;
import com.unq.eis.apibancaria.exception.CajasNoIngresadasException;
import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransferenciaTest {

    private Usuario usuario1;
    private Usuario usuario2;
    private Caja caja1;
    private Caja caja2;
    private Transferencia transferencia1;
    private Transferencia transferencia2;

    @BeforeEach
    void setUp(){
        usuario1 = new Usuario("nico@gmail.com","1234","Nicolas","Vaccaro","40.123.456");
        usuario2 = new Usuario("mati@gmail.com","4567","Matias","Alvarez","40.777.258");
        caja1 = new Caja(1L,1L,"testCaja.api",usuario1);
        caja2 = new Caja(2L, 2L, "pruebaCaja.api",usuario2);
    }

    @Test
    public void CreacionDeTransferenciaExitosa(){
        transferencia1 = new Transferencia(BigDecimal.ONE, caja1, caja2);

        assertEquals(0, transferencia1.getMontoTotal().compareTo(BigDecimal.ONE));
        assertEquals(1L, transferencia1.getCajaOrigen().getNroCaja());
        assertEquals(2L, transferencia1.getCajaDestino().getNroCaja());
    }

    @Test
    public void ErrorCreacionDeTransferenciaConMontoMenorEIgualACero(){

        assertThrows(MontoInvalidoException.class, ()->{transferencia1 = new Transferencia(BigDecimal.ZERO,caja1, caja2);});
        assertThrows(MontoInvalidoException.class, ()->{transferencia2 = new Transferencia(BigDecimal.valueOf(-1L),caja1, caja2);});
    }

    @Test
    public void ErrorCreacionDeTransferenciaConCajaVacias(){

        assertThrows(NullPointerException.class, ()->{transferencia1 = new Transferencia(BigDecimal.ONE,null, caja2);});
        assertThrows(NullPointerException.class, ()->{transferencia2 = new Transferencia(BigDecimal.ONE,caja1,null);});
    }

    @Test
    public void ErrorCreacionTransferenciaConCajaOrigenYDEstinoIgual(){

        assertThrows(CajasIgualesException.class, ()->{transferencia1 = new Transferencia(BigDecimal.ONE,caja1, caja1);});
    }
}
