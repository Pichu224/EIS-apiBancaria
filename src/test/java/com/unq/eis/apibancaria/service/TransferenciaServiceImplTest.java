package com.unq.eis.apibancaria.service;

import java.math.BigDecimal;
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
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.impl.TransferenciaServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceImplTest {

    @Mock
    private TransferenciaDAO transferenciaDAO;

    @InjectMocks
    private TransferenciaServiceImpl transferenciaService;

    private Usuario usuario;
    private Caja cajaOrigen;
    private Caja cajaDestino;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        cajaOrigen = new Caja(1L, "origen", usuario);
        cajaDestino = new Caja(2L, "destino", usuario);
    }

    @Test
    void crearYRecuperarTransferenciaExitosa() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(150), cajaOrigen, cajaDestino);
        transferencia.setIdTransferencia(1L);

        when(transferenciaDAO.save(any(Transferencia.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transferenciaDAO.findById(eq(transferencia.getIdTransferencia()))).thenReturn(Optional.of(transferencia));

        transferenciaService.crear(transferencia);
        Transferencia recuperada = transferenciaService.recuperar(transferencia.getIdTransferencia());

        assertEquals(BigDecimal.valueOf(150), recuperada.getMontoTotal());
        assertEquals(cajaOrigen.getIdCaja(), recuperada.getCajaOrigen().getIdCaja());
        assertEquals(cajaDestino.getIdCaja(), recuperada.getCajaDestino().getIdCaja());
    }

    @Test
    void actualizarTransferenciaExistente() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(75), cajaOrigen, cajaDestino);
        transferencia.setIdTransferencia(2L);

        when(transferenciaDAO.existsById(eq(transferencia.getIdTransferencia()))).thenReturn(true);
        when(transferenciaDAO.save(any(Transferencia.class))).thenAnswer(invocation -> invocation.getArgument(0));

        transferencia.setMontoTotal(BigDecimal.valueOf(125));
        transferenciaService.actualizar(transferencia);

        assertEquals(BigDecimal.valueOf(125), transferencia.getMontoTotal());
    }

    @Test
    void eliminarTransferenciaPersistida() {
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(50), cajaOrigen, cajaDestino);
        transferencia.setIdTransferencia(3L);

        when(transferenciaDAO.existsById(eq(transferencia.getIdTransferencia()))).thenReturn(true);
        doNothing().when(transferenciaDAO).deleteById(eq(transferencia.getIdTransferencia()));

        transferenciaService.eliminar(transferencia.getIdTransferencia());
    }

    @Test
    void recuperarTransferenciaInexistenteLanzaException() {
        when(transferenciaDAO.findById(eq(999L))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> transferenciaService.recuperar(999L));
    }
}
