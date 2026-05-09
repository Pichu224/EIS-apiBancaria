package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.ContraseñaVaciaException;
import com.unq.eis.apibancaria.exception.MailInvalidoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertEquals("123",userTest.getContraseña());
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
        assertThrows(ContraseñaVaciaException.class, () -> {new Usuario("nico@gmail.com",null,"Nicolas","Vaccaro","40.123.456");});
        assertThrows(ContraseñaVaciaException.class, () -> {new Usuario("nico@gmail.com","","Nicolas","Vaccaro","40.123.456");});
    }

    @Test
    public void SettearLosValoresDeUnUsuario(){

        userTest.setIdUsuario(1L);
        userTest.setEmail("nico@yahoo.com");
        userTest.setContraseña("456");
        userTest.setNombre("Nico");
        userTest.setApellido("Vaqui");
        userTest.setDni("40.999.123");

        assertNotEquals(userTest.getIdUsuario(), null);
        assertNotEquals(userTest.getEmail(), "nico@gmail.com");
        assertNotEquals(userTest.getContraseña(), "123");
        assertNotEquals(userTest.getNombre(), "Nicolas");
        assertNotEquals(userTest.getApellido(), "Vaccaro");
        assertNotEquals(userTest.getDni(), "40.123.456");
    }
}
