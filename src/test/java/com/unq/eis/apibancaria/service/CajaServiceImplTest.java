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
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class CajaServiceImplTest {

    @Mock
    private CajaDAO cajaDAO;

    @InjectMocks
    private CajaServiceImpl cajaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
    }

    @Test
    void crearYRecuperarCajaExitosa() {
        Caja caja = new Caja(1L, "testCaja", usuario);

        when(cajaDAO.save(any(Caja.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cajaDAO.findById(eq(caja.getIdCaja()))).thenReturn(Optional.of(caja));

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
        caja.setIdCaja(2L);

        when(cajaDAO.existsById(eq(caja.getIdCaja()))).thenReturn(true);
        when(cajaDAO.save(any(Caja.class))).thenAnswer(invocation -> invocation.getArgument(0));

        caja.setAlias("aliasActualizado");
        caja.setSaldo(BigDecimal.valueOf(100));
        caja.setTipoCaja(TipoCaja.CajaCorriente);

        cajaService.actualizar(caja);

        assertEquals("aliasActualizado", caja.getAlias());
        assertEquals(BigDecimal.valueOf(100), caja.getSaldo());
        assertEquals(TipoCaja.CajaCorriente, caja.getTipoCaja());
    }

    @Test
    void eliminarCajaPersistida() {
        Caja caja = new Caja(3L, "aliasEliminar", usuario);
        caja.setIdCaja(3L);

        when(cajaDAO.existsById(eq(caja.getIdCaja()))).thenReturn(true);
        doNothing().when(cajaDAO).deleteById(eq(caja.getIdCaja()));

        cajaService.eliminar(caja.getIdCaja());
    }

    @Test
    void recuperarCajaInexistenteLanzaException() {
        when(cajaDAO.findById(eq(999L))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cajaService.recuperar(999L));
    }
}
