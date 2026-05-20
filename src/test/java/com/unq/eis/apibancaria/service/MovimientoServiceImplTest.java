package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApibancariaApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MovimientoServiceImplTest {

    @Autowired
    private MovimientoService movimientoService;

    @Autowired
    private MovimientoDAO movimientoDAO;

    @Autowired
    private CajaDAO cajaDAO;

    @Autowired
    private UsuarioDAO usuarioDAO;

    private Usuario usuario;
    private Caja caja;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuarioDAO.save(usuario);
        caja = new Caja(1L, "testCaja", usuario);
        cajaDAO.save(caja);
    }

    @AfterEach
    void cleanUp() {
        movimientoDAO.deleteAll();
        cajaDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

    @Test
    void crearYRecuperarMovimientoExitosa() {
        Movimiento movimiento = new Movimiento(1L, caja, "depósito");
        movimiento.setMonto(BigDecimal.valueOf(150));
        movimiento.setFechaRealizado(LocalDateTime.now());

        movimientoService.crear(movimiento);
        Movimiento recuperado = movimientoService.recuperar(movimiento.getIdMovimiento());

        assertEquals(BigDecimal.valueOf(150), recuperado.getMonto());
        assertEquals("depósito", recuperado.getDescripcion());
        assertEquals(caja.getIdCaja(), recuperado.getCajaUtilizada().getIdCaja());
    }

    @Test
    void actualizarMovimientoExistente() {
        Movimiento movimiento = new Movimiento(2L, caja, "retiro");
        movimiento.setMonto(BigDecimal.valueOf(75));
        movimiento.setFechaRealizado(LocalDateTime.now());
        movimientoService.crear(movimiento);

        movimiento.setMonto(BigDecimal.valueOf(125));
        movimiento.setDescripcion("retiro corregido");
        movimientoService.actualizar(movimiento);

        Movimiento actual = movimientoService.recuperar(movimiento.getIdMovimiento());
        assertEquals(BigDecimal.valueOf(125), actual.getMonto());
        assertEquals("retiro corregido", actual.getDescripcion());
    }

    @Test
    void eliminarMovimientoPersistido() {
        Movimiento movimiento = new Movimiento(3L, caja, "transferencia");
        movimiento.setMonto(BigDecimal.valueOf(50));
        movimiento.setFechaRealizado(LocalDateTime.now());
        movimientoService.crear(movimiento);

        movimientoService.eliminar(movimiento.getIdMovimiento());
        assertThrows(EntityNotFoundException.class, () -> movimientoService.recuperar(movimiento.getIdMovimiento()));
    }

    @Test
    void recuperarMovimientoInexistenteLanzaException() {
        assertThrows(EntityNotFoundException.class, () -> movimientoService.recuperar(999L));
    }
}
