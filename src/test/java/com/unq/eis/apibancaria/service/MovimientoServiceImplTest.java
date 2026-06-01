package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.impl.MovimientoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceImplTest {

    @Mock
    private MovimientoDAO movimientoDAO;

    @Mock
    private CajaDAO cajaDAO;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    public void recuperarMovimientosDeCajaRetornaMovimientos() {

        Long idCaja = 1L;

        Caja caja = mock(Caja.class);
        Movimiento movimiento1 = mock(Movimiento.class);
        Movimiento movimiento2 = mock(Movimiento.class);
        List<Movimiento> movimientos = List.of(movimiento1, movimiento2);

        when(cajaDAO.existsById(idCaja)).thenReturn(true);

        when(movimientoDAO.findByCajaUtilizada_IdCaja(idCaja)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.recuperarMovimientosDeCaja(idCaja);

        assertEquals(2, resultado.size());
        assertEquals(movimientos, resultado);

        verify(cajaDAO).existsById(idCaja);
        verify(movimientoDAO).findByCajaUtilizada_IdCaja(idCaja);
    }

    @Test
    void recuperarMovimientosDeCajaFallaSiCajaNoExiste() {

        Long idCaja = 1L;

        when(cajaDAO.existsById(idCaja)).thenReturn(false);

        CajaInexistenteException exception = assertThrows( CajaInexistenteException.class, () -> movimientoService.recuperarMovimientosDeCaja(idCaja));

        assertEquals("Caja no Encontrada!", exception.getMessage());

        verify(cajaDAO).existsById(idCaja);

        verify(movimientoDAO, never()).findByCajaUtilizada_IdCaja(anyLong());
    }
}
