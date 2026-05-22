package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.ContraseniaVaciaException;
import com.unq.eis.apibancaria.exception.MailInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.support.CustomSQLErrorCodesTranslation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    private Usuario userTest;

    @BeforeEach
    void setUp(){
        userTest = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
    }

    @Test
    public void CreacionUsuario(){
        assertEquals("nico@gmail.com", userTest.getEmail());
        assertEquals("123",userTest.getContrasenia());
        assertEquals("Nicolas", userTest.getNombre());
        assertEquals("Vaccaro",userTest.getApellido());
        assertEquals("40.123.456", userTest.getDni());
    }

    @Test
    public void ErrorAlCrearUnUsuarioConEmailVacioOIncorrecto(){
        assertThrows(MailInvalidoException.class, () -> {new Usuario("","123","Nicolas","Vaccaro","40.123.456");});
        assertThrows(MailInvalidoException.class, () -> {new Usuario(null,"123","Nicolas","Vaccaro","40.123.456");});
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
        userTest.addCaja( caja );
        assertEquals(BigDecimal.ZERO, userTest.consultarSaldo(caja));
        caja.depositar(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, userTest.consultarSaldo(caja));
    }
}
