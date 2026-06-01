package com.unq.eis.apibancaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.eis.apibancaria.controller.dto.response.CajaInfoResponse;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import com.unq.eis.apibancaria.exception.SaldoInsuficienteException;
import com.unq.eis.apibancaria.exception.TransferenciaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.impl.TransferenciaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransferenciaControllerTest {

    private static final String BASE_URL = "/transferencias";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TransferenciaServiceImpl transferenciaService;

    @InjectMocks
    private TransferenciaController transferenciaController;

    private Usuario usuario;
    private Caja origen;
    private Caja destino;
    private Transferencia transferencia;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transferenciaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        this.usuario = new Usuario();
        usuario.setIdUsuario(1L);

        this.origen = new Caja(1111L, "Origen", usuario);
        origen.setIdCaja(1L);

        this.destino = new Caja(2222L, "Destino", usuario);
        destino.setIdCaja(2L);

        this.transferencia = new Transferencia(BigDecimal.valueOf(500), origen, destino);
        transferencia.setIdTransferencia(10L);
    }

    @Test
    public void transferirDinero() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja Origen", TipoCaja.CajaAhorro, BigDecimal.valueOf(500) );

        mockMvc.perform(
                        post(BASE_URL + "/1/transferir/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        verify(transferenciaService).tranferir(1L, 2L, BigDecimal.valueOf(500) );
    }

    @Test
    public void recuperarTransferencia() throws Exception {

        when(transferenciaService.recuperar(10L)).thenReturn(transferencia);

        mockMvc.perform(get(BASE_URL + "/10")).andExpect(status().isOk());

        verify(transferenciaService).recuperar(10L);
    }

    @Test
    public void recuperarTransferenciaDevuelveDatosCorrectos() throws Exception {

        when(transferenciaService.recuperar(10L)).thenReturn(transferencia);

        mockMvc.perform(get(BASE_URL + "/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTransferencia").value(10))
                .andExpect(jsonPath("$.montoTotal").value(500))
                .andExpect(jsonPath("$.idCajaOrigen").value(1))
                .andExpect(jsonPath("$.idCajaDestino").value(2));
    }

    @Test
    public void transferirCajaOrigenInexistente() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja", TipoCaja.CajaAhorro, BigDecimal.valueOf(500) );

        doThrow(new CajaInexistenteException("Caja no encontrada"))
                .when(transferenciaService)
                .tranferir(eq(1L), eq(2L), any(BigDecimal.class)
                );

        mockMvc.perform(
                        post(BASE_URL + "/1/transferir/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void recuperarTransferenciaInexistente() throws Exception {

        when(transferenciaService.recuperar(99L))
                .thenThrow( new TransferenciaInexistenteException("Transferencia no encontrada")
                );

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void transferirSaldoInsuficiente() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L,12345L,"Caja", TipoCaja.CajaAhorro, BigDecimal.valueOf(100000) );

        doThrow(new SaldoInsuficienteException("Saldo insuficiente"))
                .when(transferenciaService)
                .tranferir(eq(1L), eq(2L), any(BigDecimal.class)
                );

        mockMvc.perform(
                        post(BASE_URL + "/1/transferir/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void transferirMontoInvalido() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja", TipoCaja.CajaAhorro, BigDecimal.ZERO );

        doThrow(new MontoInvalidoException("Monto inválido"))
                .when(transferenciaService)
                .tranferir(eq(1L), eq(2L), any(BigDecimal.class)
                );

        mockMvc.perform(
                        post(BASE_URL + "/1/transferir/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());
    }
}
