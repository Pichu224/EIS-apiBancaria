package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApibancariaApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CajaServiceImplTest {

    @Autowired
    private CajaService cajaService;

    @Autowired
    private CajaDAO cajaDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioDAO.save(usuario);
    }

    @AfterEach
    void cleanUp() {
        cajaDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

    @Test
    void crearYRecuperarCajaExitosa() {
        Caja caja = new Caja(1L, "testCaja", usuario);
        cajaService.crear(caja);

        Caja recuperada = cajaService.recuperar(caja.getIdCaja());

        assertEquals(1L, recuperada.getNroCaja());
        assertEquals("testCaja", recuperada.getAlias());
        assertEquals(BigDecimal.ZERO, recuperada.getSaldo());
        assertEquals(TipoCaja.CajaAhorro, recuperada.getTipoCaja());
        assertEquals(usuario.getIdUsuario(), recuperada.getUsuario().getIdUsuario());
    }

    @Test
    void actualizarCajaExistente() {
        Caja caja = new Caja(2L, "aliasOriginal", usuario);
        cajaService.crear(caja);

        caja.setAlias("aliasActualizado");
        caja.setSaldo(BigDecimal.valueOf(100));
        caja.setTipoCaja(TipoCaja.CajaCorriente);

        cajaService.actualizar(caja);

        Caja actualizada = cajaService.recuperar(caja.getIdCaja());
        assertEquals("aliasActualizado", actualizada.getAlias());
        assertEquals(BigDecimal.valueOf(100), actualizada.getSaldo());
        assertEquals(TipoCaja.CajaCorriente, actualizada.getTipoCaja());
    }

    @Test
    void eliminarCajaPersistida() {
        Caja caja = new Caja(3L, "aliasEliminar", usuario);
        cajaService.crear(caja);

        cajaService.eliminar(caja.getIdCaja());
        assertThrows(EntityNotFoundException.class, () -> cajaService.recuperar(caja.getIdCaja()));
    }

    @Test
    void recuperarCajaInexistenteLanzaException() {
        assertThrows(EntityNotFoundException.class, () -> cajaService.recuperar(999L));
    }
}
