package com.unq.eis.apibancaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({UsuarioController.class, CajaController.class, MovimientoController.class, TransferenciaController.class})
class ControllerLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private CajaService cajaService;

    @MockBean
    private MovimientoService movimientoService;

    @MockBean
    private TransferenciaService transferenciaService;

    @Test
    void crearUsuarioEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        doNothing().when(usuarioService).crear(any(Usuario.class));

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("nico@gmail.com"));
    }

    @Test
    void recuperarUsuarioEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        when(usuarioService.recuperar(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(1))
                .andExpect(jsonPath("$.email").value("nico@gmail.com"));
    }

    @Test
    void crearCajaEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        Caja caja = new Caja(1L, "testCaja", usuario);
        doNothing().when(cajaService).crear(any(Caja.class));

        mockMvc.perform(post("/api/cajas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caja)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.alias").value("testCaja"));
    }

    @Test
    void recuperarCajaEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        Caja caja = new Caja(1L, "testCaja", usuario);
        caja.setIdCaja(1L);
        when(cajaService.recuperar(1L)).thenReturn(caja);

        mockMvc.perform(get("/api/cajas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCaja").value(1))
                .andExpect(jsonPath("$.alias").value("testCaja"));
    }

    @Test
    void crearMovimientoEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        Caja caja = new Caja(1L, "testCaja", usuario);
        caja.setIdCaja(1L);
        Movimiento movimiento = new Movimiento(1L, caja, "test");
        movimiento.setMonto(BigDecimal.valueOf(100));
        movimiento.setFechaRealizado(LocalDateTime.now());
        doNothing().when(movimientoService).crear(any(Movimiento.class));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movimiento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.descripcion").value("test"));
    }

    @Test
    void crearTransferenciaEndpoint() throws Exception {
        Usuario usuario = new Usuario("nico@gmail.com", "123", "Nicolas", "Vaccaro", "40.123.456");
        usuario.setIdUsuario(1L);
        Caja cajaOrigen = new Caja(1L, "origen", usuario);
        Caja cajaDestino = new Caja(2L, "destino", usuario);
        Transferencia transferencia = new Transferencia(BigDecimal.valueOf(50), cajaOrigen, cajaDestino);
        doNothing().when(transferenciaService).crear(any(Transferencia.class));

        mockMvc.perform(post("/api/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferencia)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.montoTotal").value(50));
    }
}
