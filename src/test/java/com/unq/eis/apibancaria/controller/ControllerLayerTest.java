package com.unq.eis.apibancaria.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;

@ExtendWith(MockitoExtension.class)
class ControllerLayerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private CajaService cajaService;

    @Mock
    private MovimientoService movimientoService;

    @Mock
    private TransferenciaService transferenciaService;

    @InjectMocks
    private UsuarioController usuarioController;

    @InjectMocks
    private CajaController cajaController;

    @InjectMocks
    private MovimientoController movimientoController;

    @InjectMocks
    private TransferenciaController transferenciaController;

    private Usuario usuario;
    private Caja caja;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        caja = new Caja(1L, "testCaja", usuario);
        caja.setIdCaja(1L);
    }

    @Test
    void crearUsuarioEndpoint() {
        doNothing().when(usuarioService).crear(any(Usuario.class));

        ResponseEntity<Usuario> response = usuarioController.crear(usuario);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("nico@gmail.com", response.getBody().getEmail());
    }

    @Test
    void recuperarUsuarioEndpoint() {
        when(usuarioService.recuperar(1L)).thenReturn(usuario);

        ResponseEntity<Usuario> response = usuarioController.recuperar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getIdUsuario());
        assertEquals("nico@gmail.com", response.getBody().getEmail());
    }

    @Test
    void crearCajaEndpoint() {
        doNothing().when(cajaService).crear(any(Caja.class));

        ResponseEntity<Caja> response = cajaController.crear(caja);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testCaja", response.getBody().getAlias());
    }

    @Test
    void recuperarCajaEndpoint() {
        when(cajaService.recuperar(1L)).thenReturn(caja);

        ResponseEntity<Caja> response = cajaController.recuperar(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getIdCaja());
        assertEquals("testCaja", response.getBody().getAlias());
    }

    @Test
    void crearMovimientoEndpoint() {
        Movimiento movimiento = new Movimiento(1L, caja, "test");
        movimiento.setMonto(BigDecimal.valueOf(100));
        movimiento.setFechaRealizado(LocalDateTime.now());

        doNothing().when(movimientoService).crear(any(Movimiento.class));

        ResponseEntity<Movimiento> response = movimientoController.crear(movimiento);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test", response.getBody().getDescripcion());
    }

    @Test
    void crearTransferenciaEndpoint() {
        Caja cajaOrigen = new Caja(1L, "origen", usuario);
        Caja cajaDestino = new Caja(2L, "destino", usuario);
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(50), cajaOrigen, cajaDestino);
        doNothing().when(transferenciaService).crear(any(Transferencia.class));

        ResponseEntity<Transferencia> response = transferenciaController.crear(transferencia);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(BigDecimal.valueOf(50), response.getBody().getMontoTotal());
    }
}
