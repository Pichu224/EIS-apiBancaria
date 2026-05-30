package com.unq.eis.apibancaria.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.IdNuloException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.UsuarioServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private UsuarioServiceImpl serviceUsuario;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;

    @BeforeEach
    void setUp(){
        usuarioTest1 = new Usuario();
        usuarioTest1.setEmail("nico@gmail.com");
        usuarioTest1.setContrasenia("1234");
        usuarioTest1.setNombre("Nicolas");
        usuarioTest1.setApellido("Vaccaro");
        usuarioTest1.setDni("40.123.456");

        usuarioTest2 = new Usuario();
        usuarioTest2.setEmail("nico@yahoo.com");
        usuarioTest2.setContrasenia("456");
        usuarioTest2.setNombre("Matias");
        usuarioTest2.setApellido("Boldo");
        usuarioTest2.setDni("40.777.361");
    }

    @Test
    public void CreacionyRecuperarDelUsuarioExitosa(){
        usuarioTest1.setIdUsuario(1L);

        when(usuarioDAO.existsByEmail(usuarioTest1.getEmail())).thenReturn(false);
        when(usuarioDAO.save(usuarioTest1)).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setIdUsuario(1L);
            return u;
        });
        when(usuarioDAO.findById(1L)).thenReturn(Optional.of(usuarioTest1));

        serviceUsuario.crear(usuarioTest1);

        Usuario usuarioRecuperado = serviceUsuario.recuperar(1L);

        assertEquals(usuarioTest1.getEmail(), usuarioRecuperado.getEmail());
        assertEquals(usuarioTest1.getContrasenia(), usuarioRecuperado.getContrasenia());
        assertEquals(usuarioTest1.getNombre(), usuarioRecuperado.getNombre());
        assertEquals(usuarioTest1.getApellido(), usuarioRecuperado.getApellido());
        assertEquals(usuarioTest1.getDni(), usuarioRecuperado.getDni());
    }

    @Test
    public void CrearUsuarioConMailYaExistente(){
        when(usuarioDAO.existsByEmail(anyString())).thenReturn(true);
        usuarioTest1.setEmail("nico@gmail.com");
        assertThrows(EmailYaExistenteException.class, () -> serviceUsuario.crear(usuarioTest1));
    }

    @Test
    public void ActualizarUnUsuarioPersistido(){
        usuarioTest1.setIdUsuario(1L);
        when(usuarioDAO.findById(1L)).thenReturn(Optional.of(usuarioTest1));

        usuarioTest1.setContrasenia("4567");
        usuarioTest1.setNombre("Nico");
        usuarioTest1.setApellido("Vaqui");
        usuarioTest1.setDni("40.321.654");

        serviceUsuario.actualizar(1L, usuarioTest1);

        Usuario actualizado = serviceUsuario.recuperar(1L);

        assertEquals(usuarioTest1.getIdUsuario(), actualizado.getIdUsuario());
        assertEquals(usuarioTest1.getEmail(), actualizado.getEmail());
        assertEquals(usuarioTest1.getContrasenia(), actualizado.getContrasenia());
        assertEquals(usuarioTest1.getNombre(), actualizado.getNombre());
        assertEquals(usuarioTest1.getApellido(), actualizado.getApellido());
        assertEquals(usuarioTest1.getDni(), actualizado.getDni());
    }

    @Test
    public void ActualizarFallaPorUsuarioNoPersistido(){
        usuarioTest1.setEmail("nico@gmail.com");
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.actualizar(null, usuarioTest1));
    }

    @Test
    public void ActualizarFallaPorUsuarioConIdNoPersistido(){
        usuarioTest1.setIdUsuario(100L);
        when(usuarioDAO.findById(100L)).thenReturn(Optional.empty());
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.actualizar(100L, usuarioTest1));
    }

    @Test
    public void ActualizarFallaPorMailYaExistenPorOtroUsuario(){
        usuarioTest1.setIdUsuario(1L);
        usuarioTest2.setIdUsuario(2L);

        when(usuarioDAO.findById(2L)).thenReturn(Optional.of(usuarioTest2));
        when(usuarioDAO.existsByEmail(usuarioTest1.getEmail())).thenReturn(true);

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setIdUsuario(2L);
        usuarioActualizado.setEmail(usuarioTest1.getEmail());
        usuarioActualizado.setContrasenia(usuarioTest2.getContrasenia());
        usuarioActualizado.setNombre(usuarioTest2.getNombre());
        usuarioActualizado.setApellido(usuarioTest2.getApellido());
        usuarioActualizado.setDni(usuarioTest2.getDni());

        assertThrows(EmailYaExistenteException.class, () -> serviceUsuario.actualizar(2L, usuarioActualizado));
    }

    @Test
    public void RecuperarUsuarioSinSerPersistido(){
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(null));
    }

    @Test
    public void RecuperarUsuarioConIdInexistente(){
        when(usuarioDAO.findById(100L)).thenReturn(Optional.empty());
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(100L));
    }

    @Test
    public void BorrarUsuarioPersistido(){
        when(usuarioDAO.findById(1L)).thenReturn(Optional.of(usuarioTest1));
        serviceUsuario.eliminar(1L);
        when(usuarioDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.recuperar(1L));
    }

    @Test
    public void BorrarUsuarioQueNoPosseId(){
        assertThrows(UsuarioInexistenteException.class, () -> serviceUsuario.eliminar(null));
    }

}
