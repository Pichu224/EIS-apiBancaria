package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.UsuarioServiceImpl;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioServiceImpl serviceUsuario;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;

    @BeforeEach
    void setUp() {
        // Mockito inyecta el mock en la implementación
    }

    @Test
    public void CreacionyRecuperarDelUsuarioExitosa() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(1L);

        when(usuarioDAO.existsByEmail(usuarioTest1.getEmail())).thenReturn(false);
        when(usuarioDAO.save(usuarioTest1)).thenAnswer(invocation -> invocation.getArgument(0));
        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.of(usuarioTest1));

        serviceUsuario.crear(usuarioTest1);
        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContrasenia(), usuarioTest2.getContrasenia());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());
    }

    @Test
    public void CrearUsuarioConMailYaExistente() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");

        when(usuarioDAO.existsByEmail(usuarioTest1.getEmail())).thenReturn(true);

        assertThrows(EmailYaExistenteException.class, () -> serviceUsuario.crear(usuarioTest1));
    }

    @Test
    public void ActualizarUnUsuarioPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(1L);

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.of(usuarioTest1));
        when(usuarioDAO.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        usuarioTest1.setContrasenia("456");
        usuarioTest1.setNombre("Nico");
        usuarioTest1.setApellido("Vaqui");
        usuarioTest1.setDni("40.321.654");

        serviceUsuario.actualizar(usuarioTest1);
        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getIdUsuario(), usuarioTest2.getIdUsuario());
        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContrasenia(), usuarioTest2.getContrasenia());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());
    }

    @Test
    public void ActualizarFallaPorUsuarioNoPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(1L);

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.actualizar(usuarioTest1));
    }

    @Test
    public void ActualizarFallaPorUsuarioConIdNoPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(100L);

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.actualizar(usuarioTest1));
    }

    @Test
    public void RecuperarUsuarioPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(1L);

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.of(usuarioTest1));

        usuarioTest2 = serviceUsuario.recuperar(usuarioTest1.getIdUsuario());

        assertEquals(usuarioTest1.getIdUsuario(), usuarioTest2.getIdUsuario());
        assertEquals(usuarioTest1.getEmail(), usuarioTest2.getEmail());
        assertEquals(usuarioTest1.getContrasenia(), usuarioTest2.getContrasenia());
        assertEquals(usuarioTest1.getNombre(), usuarioTest2.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioTest2.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioTest2.getDni());
    }

    @Test
    public void RecuperarUsuarioSinSerPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenThrow(new InvalidDataAccessApiUsageException("El usuario no posee id!"));

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(usuarioTest1.getIdUsuario()));
    }

    @Test
    public void RecuperarUsuarioConIdInexistente() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(100L);

        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(usuarioTest1.getIdUsuario()));
    }

    @Test
    public void BorrarUsuarioPersistido() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioTest1.setIdUsuario(1L);

        doNothing().when(usuarioDAO).deleteById(eq(usuarioTest1.getIdUsuario()));
        when(usuarioDAO.findById(eq(usuarioTest1.getIdUsuario()))).thenReturn(Optional.empty());

        serviceUsuario.eliminar(usuarioTest1.getIdUsuario());

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(usuarioTest1.getIdUsuario()));
    }

    @Test
    public void BorrarUsuarioQueNoPosseId() {
        usuarioTest1 = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");

        doThrow(new InvalidDataAccessApiUsageException("El usuario no posee id!"))
                .when(usuarioDAO).deleteById(eq(usuarioTest1.getIdUsuario()));

        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.eliminar(usuarioTest1.getIdUsuario()));
    }
}
