package com.unq.eis.apibancaria.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.impl.MovimientoServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    @Mock
    private MovimientoDAO movimientoDAO;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    private Usuario usuario;
    private Caja caja;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        caja = new Caja(1L, "testCaja", usuario);
    }

    @Test
    void crearYRecuperarMovimientoExitosa() {
        Movimiento movimiento = new Movimiento(1L, caja, "depósito");
        movimiento.setIdMovimiento(1L);
        movimiento.setMonto(BigDecimal.valueOf(150));
        movimiento.setFechaRealizado(LocalDateTime.now());

        when(movimientoDAO.save(any(Movimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(movimientoDAO.findById(eq(movimiento.getIdMovimiento()))).thenReturn(Optional.of(movimiento));

        movimientoService.crear(movimiento);
        Movimiento recuperado = movimientoService.recuperar(movimiento.getIdMovimiento());

        assertEquals(BigDecimal.valueOf(150), recuperado.getMonto());
        assertEquals("depósito", recuperado.getDescripcion());
        assertEquals(caja.getIdCaja(), recuperado.getCajaUtilizada().getIdCaja());
    }

    @Test
    void actualizarMovimientoExistente() {
        Movimiento movimiento = new Movimiento(2L, caja, "retiro");
        movimiento.setIdMovimiento(2L);
        movimiento.setMonto(BigDecimal.valueOf(75));
        movimiento.setFechaRealizado(LocalDateTime.now());

        when(movimientoDAO.existsById(eq(movimiento.getIdMovimiento()))).thenReturn(true);
        when(movimientoDAO.save(any(Movimiento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        movimiento.setMonto(BigDecimal.valueOf(125));
        movimiento.setDescripcion("retiro corregido");
        movimientoService.actualizar(movimiento);

        assertEquals(BigDecimal.valueOf(125), movimiento.getMonto());
        assertEquals("retiro corregido", movimiento.getDescripcion());
    }

    @Test
    void eliminarMovimientoPersistido() {
        Movimiento movimiento = new Movimiento(3L, caja, "transferencia");
        movimiento.setIdMovimiento(3L);
        movimiento.setMonto(BigDecimal.valueOf(50));
        movimiento.setFechaRealizado(LocalDateTime.now());

        when(movimientoDAO.existsById(eq(movimiento.getIdMovimiento()))).thenReturn(true);
        doNothing().when(movimientoDAO).deleteById(eq(movimiento.getIdMovimiento()));

        movimientoService.eliminar(movimiento.getIdMovimiento());
    }

    @Test
    void recuperarMovimientoInexistenteLanzaException() {
        when(movimientoDAO.findById(eq(999L))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> movimientoService.recuperar(999L));
    }
}
