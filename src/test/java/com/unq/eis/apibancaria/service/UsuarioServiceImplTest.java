package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.MailInvalidoException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.UsuarioServiceImpl;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = ApibancariaApplication.class)
public class UsuarioServiceImplTest {

    @Autowired private UsuarioService serviceUsuario;
    @Autowired private UsuarioDAO usuarioDAO;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;

    @BeforeEach
    void setUp(){
        serviceUsuario = new UsuarioServiceImpl(usuarioDAO);
    }

    @Test
    public void CreacionyRecuperarDelUsuarioExitosa(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");

        serviceUsuario.crear(usuarioTest1);

        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContraseña(), usuarioTest2.getContraseña());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());

    }
    @Test
    public void CrearUsuarioConMailYaExistente(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuarioTest2 = new Usuario("nico@gmail.com","456","Nicolas","Vaccaro","40.123.456");

        serviceUsuario.crear(usuarioTest1);
        assertThrows(EmailYaExistenteException.class, () -> {serviceUsuario.crear(usuarioTest2);});
    }

    @Test
    public void ActualizarUnUsuarioPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        serviceUsuario.crear(usuarioTest1);

        usuarioTest1.setContraseña("456");
        usuarioTest1.setNombre("Nico");
        usuarioTest1.setApellido("Vaqui");
        usuarioTest1.setDni("40.321.654");

        serviceUsuario.actualizar(usuarioTest1);

        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getIdUsuario(),usuarioTest2.getIdUsuario());
        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContraseña(), usuarioTest2.getContraseña());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());

    }

    @Test
    public void ActualizarFallaPorUsuarioNoPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.actualizar(usuarioTest1);});
    }

    @Test
    public void ActualizarFallaPorUsuarioConIdNoPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuarioTest1.setIdUsuario(100L);

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.actualizar(usuarioTest1);});
    }

    @Test
    public void RecuperarUsuarioPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        serviceUsuario.crear(usuarioTest1);

        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getIdUsuario(),usuarioTest2.getIdUsuario());
        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContraseña(), usuarioTest2.getContraseña());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());

    }

    @Test
    public void RecuperarUsuarioSinSerPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.recuperar(usuarioTest1.getIdUsuario());});
    }

    @Test
    public void RecuperarUsuarioConIdInexistente(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuarioTest1.setIdUsuario(100L);

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.recuperar(usuarioTest1.getIdUsuario());});
    }

    @Test
    public void BorrarUsuarioPersistido(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        serviceUsuario.crear(usuarioTest1);

        serviceUsuario.eliminar(usuarioTest1.getIdUsuario());

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.recuperar(usuarioTest1.getIdUsuario());});

    }

    @Test
    public void BorrarUsuarioQueNoPosseId(){
        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");

        assertThrows(UsuarioInexistenteException.class, () -> {serviceUsuario.eliminar(usuarioTest1.getIdUsuario());});
    }

    @AfterEach
    void cleanup() {
        usuarioDAO.deleteAll();
    }
}
