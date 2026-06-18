package com.unq.eis.apibancaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.service.impl.MovimientoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MovimientoControllerTest {

    private static final String BASE_URL = "/movimientos";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private MovimientoServiceImpl movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    private Usuario usuario;
    private Caja caja;
    private Movimiento movimiento1;
    private Movimiento movimiento2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(movimientoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        this.usuario = new Usuario();
        usuario.setIdUsuario(1L);

        this.caja = new Caja(12345L, "Caja Principal", usuario);
        caja.setIdCaja(1L);

        this.movimiento1 = new Movimiento(10L, caja, BigDecimal.valueOf(1000),"54321");
        movimiento1.setIdMovimiento(1L);
        this.movimiento2 = new Movimiento(11L, caja, BigDecimal.valueOf(500), "98765");
        movimiento2.setIdMovimiento(2L);
    }

    @Test
    public void recuperarMovimientosDeCaja() throws Exception {


        List<Movimiento> movimientos = List.of(movimiento1, movimiento2);

        when(movimientoService.recuperarMovimientosDeCaja(1L)).thenReturn(movimientos);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(movimientoService)
                .recuperarMovimientosDeCaja(1L);
    }
    @Test
    public void recuperarMovimientosDeCajaDevuelveDatosCorrectos() throws Exception {

        when(movimientoService.recuperarMovimientosDeCaja(1L))
                .thenReturn(List.of(movimiento1));

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMovimiento").value(1))
                .andExpect(jsonPath("$[0].monto").value(1000))
                .andExpect(jsonPath("$[0].nroTransferencia").value(10))
                .andExpect(jsonPath("$[0].idCaja").value(1));

        verify(movimientoService).recuperarMovimientosDeCaja(1L);
    }

    @Test
    public void recuperarMovimientosDeCajaInexistente() throws Exception {

        when(movimientoService.recuperarMovimientosDeCaja(99L)).thenThrow( new CajaInexistenteException("Caja no Encontrada!"));

        mockMvc.perform(get(BASE_URL + "/99")).andExpect(status().isNotFound());

        verify(movimientoService).recuperarMovimientosDeCaja(99L);
    }
}
