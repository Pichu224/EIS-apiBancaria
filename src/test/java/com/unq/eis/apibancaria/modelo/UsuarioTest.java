package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.ContraseniaVaciaException;
import com.unq.eis.apibancaria.exception.MailInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario userTest;
    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp(){
        userTest = new Usuario("nico@gmail.com","1234","Nicolas","Vaccaro","40.123.456");
    }

    @Test
    public void CreacionUsuario(){

        usuario1 = new Usuario("test1@gmail.com", "12345");
        usuario2 = new Usuario(1L,"test2@gmail.com","asdfg");

        assertEquals("nico@gmail.com", userTest.getEmail());
        assertEquals("1234",userTest.getContrasenia());
        assertEquals("Nicolas", userTest.getNombre());
        assertEquals("Vaccaro",userTest.getApellido());
        assertEquals("40.123.456", userTest.getDni());

        assertEquals("test1@gmail.com", usuario1.getEmail());
        assertEquals("12345",usuario1.getContrasenia());

        assertEquals(1L,usuario2.getIdUsuario());
        assertEquals("test2@gmail.com", usuario2.getEmail());
        assertEquals("asdfg",usuario2.getContrasenia());

    }

    @Test
    public void ErrorAlCrearUnUsuarioConEmailVacioOIncorrecto(){
        assertThrows(MailInvalidoException.class, () -> {new Usuario("","1234","Nicolas","Vaccaro","40.123.456");});
        assertThrows(MailInvalidoException.class, () -> {new Usuario(null,"1234","Nicolas","Vaccaro","40.123.456");});
    }

    @Test
    public void ErrorAlCrearUnUsuarioConContraseñaVacia(){
        assertThrows(ContraseniaVaciaException.class, () -> {new Usuario("nico@gmail.com",null,"Nicolas","Vaccaro","40.123.456");});
        assertThrows(ContraseniaVaciaException.class, () -> {new Usuario("nico@gmail.com","","Nicolas","Vaccaro","40.123.456");});
    }

    @Test
    public void SettearLosValoresDeUnUsuario(){

        userTest.setIdUsuario(1L);
        userTest.setEmail("nico@yahoo.com");
        userTest.setContrasenia("456");
        userTest.setNombre("Nico");
        userTest.setApellido("Vaqui");
        userTest.setDni("40.999.123");

        assertNotEquals(null, userTest.getIdUsuario());
        assertNotEquals("nico@gmail.com", userTest.getEmail());
        assertNotEquals("123", userTest.getContrasenia());
        assertNotEquals("Nicolas", userTest.getNombre());
        assertNotEquals("Vaccaro", userTest.getApellido());
        assertNotEquals("40.123.456", userTest.getDni());
    }

    @Test
    public void ConsultarSaldoDeUnaCaja(){
        userTest.setIdUsuario(1L);
        Caja caja = new Caja(1L,"testCaja",userTest);
        assertThrows(CajaInexistenteException.class, () -> userTest.consultarSaldo(caja));
        userTest.addCaja(caja);
        assertEquals(BigDecimal.ZERO, userTest.consultarSaldo(caja));
        caja.depositar(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, userTest.consultarSaldo(caja));
    }

    @Test
    public void IngresarDineroAUnaCajaInexistente(){
        userTest.setIdUsuario(1L);
        Caja caja = new Caja();
        assertThrows(CajaInexistenteException.class, () -> userTest.ingresarDinero(BigDecimal.valueOf(1000), caja));
    }

    @Test
    public void IngresarSaldoDeUnaCaja(){
        userTest.setIdUsuario(1L);
        Caja caja = new Caja(1L,"testCaja",userTest);
        userTest.addCaja(caja);
        assertEquals(0, userTest.consultarSaldo(caja).doubleValue());
        userTest.ingresarDinero(BigDecimal.valueOf(1000), caja);
        assertEquals(1000, userTest.consultarSaldo(caja).doubleValue());
        caja.retirar(BigDecimal.valueOf(5));
        assertEquals(995, userTest.consultarSaldo(caja).doubleValue());
    }
    @Test
    public void ConsultarSaldoDeUnaCajaInexistente(){
        userTest.setIdUsuario(1L);
        Caja caja = new Caja();
        assertThrows(CajaInexistenteException.class, () -> userTest.consultarSaldo(caja));
    }
}
