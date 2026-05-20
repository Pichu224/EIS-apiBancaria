package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
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
class TransferenciaServiceImplTest {

    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private TransferenciaDAO transferenciaDAO;

    @Autowired
    private CajaDAO cajaDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private Usuario usuario;
    private Caja cajaOrigen;
    private Caja cajaDestino;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioDAO.save(usuario);
        cajaOrigen = new Caja(1L, "origen", usuario);
        cajaDestino = new Caja(2L, "destino", usuario);
        cajaDAO.save(cajaOrigen);
        cajaDAO.save(cajaDestino);
    }

    @AfterEach
    void cleanUp() {
        transferenciaDAO.deleteAll();
        cajaDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

    @Test
    void crearYRecuperarTransferenciaExitosa() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(150), cajaOrigen, cajaDestino);
        transferenciaService.crear(transferencia);

        Transferencia recuperada = transferenciaService.recuperar(transferencia.getIdTransferencia());

        assertEquals(BigDecimal.valueOf(150), recuperada.getMontoTotal());
        assertEquals(cajaOrigen.getIdCaja(), recuperada.getCajaOrigen().getIdCaja());
        assertEquals(cajaDestino.getIdCaja(), recuperada.getCajaDestino().getIdCaja());
    }

    @Test
    void actualizarTransferenciaExistente() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(75), cajaOrigen, cajaDestino);
        transferenciaService.crear(transferencia);

        transferencia.setMontoTotal(BigDecimal.valueOf(125));
        transferenciaService.actualizar(transferencia);

        Transferencia actualizada = transferenciaService.recuperar(transferencia.getIdTransferencia());
        assertEquals(BigDecimal.valueOf(125), actualizada.getMontoTotal());
    }

    @Test
    void eliminarTransferenciaPersistida() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(50), cajaOrigen, cajaDestino);
        transferenciaService.crear(transferencia);

        transferenciaService.eliminar(transferencia.getIdTransferencia());
        assertThrows(EntityNotFoundException.class, () -> transferenciaService.recuperar(transferencia.getIdTransferencia()));
    }

    @Test
    void recuperarTransferenciaInexistenteLanzaException() {
        assertThrows(EntityNotFoundException.class, () -> transferenciaService.recuperar(999L));
    }
}
