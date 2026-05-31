package com.unq.eis.apibancaria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.eis.apibancaria.controller.dto.request.CajaActualizarRequest;
import com.unq.eis.apibancaria.controller.dto.request.CajaRequest;
import com.unq.eis.apibancaria.controller.dto.request.UsuarioCajaRequest;
import com.unq.eis.apibancaria.controller.dto.response.CajaInfoResponse;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import com.unq.eis.apibancaria.exception.NroCajaYaExistenteException;
import com.unq.eis.apibancaria.exception.SaldoInsuficienteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CajaControllerTest {

    private static final String BASE_URL = "/cajas";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CajaServiceImpl cajaService;

    @InjectMocks
    private CajaController cajaController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(cajaController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void crearCaja() throws Exception {

        CajaRequest request = new CajaRequest(12345L, "Caja Principal", new UsuarioCajaRequest(1L, "test@test.com", "1234" ));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(cajaService).crear(any(Caja.class));
    }

    @Test
    void crearCajaConNumeroDuplicadoRetornaBadRequest() throws Exception {

        CajaRequest request =
                new CajaRequest(12345L, "Caja Principal", new UsuarioCajaRequest(1L, "test@test.com", "1234" ));

        doThrow(new NroCajaYaExistenteException("Número de caja existente"))
                .when(cajaService)
                .crear(any(Caja.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void recuperarCaja() throws Exception {

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        Caja caja = new Caja(12345L, "Caja Principal", usuario );

        caja.setIdCaja(1L);

        when(cajaService.recuperar(1L)).thenReturn(caja);

        mockMvc.perform(get(BASE_URL + "/1")).andExpect(status().isOk());

        verify(cajaService).recuperar(1L);
    }

    @Test
    void recuperarCajaInexistenteRetornaBadRequest() throws Exception {

        when(cajaService.recuperar(99L))
                .thenThrow(new CajaInexistenteException("Caja inexistente"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound());

        verify(cajaService).recuperar(99L);
    }

    @Test
    void actualizarCaja() throws Exception {

        CajaActualizarRequest request = new CajaActualizarRequest(99999L, "Nuevo Alias", TipoCaja.CajaCorriente );

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        Caja cajaActualizada = new Caja(99999L, "Nuevo Alias", usuario );

        cajaActualizada.setIdCaja(1L);
        cajaActualizada.setTipoCaja(TipoCaja.CajaCorriente);

        when(cajaService.actualizar(eq(1L), any(Caja.class))).thenReturn(cajaActualizada);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cajaService).actualizar(eq(1L), any(Caja.class));
    }

    @Test
    void eliminarCaja() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/1")).andExpect(status().isNoContent());

        verify(cajaService).eliminar(1L);
    }

    @Test
    void depositarMonto() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja Principal", TipoCaja.CajaAhorro, BigDecimal.valueOf(500) );

        mockMvc.perform(put(BASE_URL + "/1/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cajaService).depositar(1L, BigDecimal.valueOf(500));
    }

    @Test
    void retirarMonto() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja Principal", TipoCaja.CajaAhorro, BigDecimal.valueOf(250) );

        mockMvc.perform(put(BASE_URL + "/1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(cajaService).retirar(1L, BigDecimal.valueOf(250));
    }

    @Test
    void depositarMontoInvalido() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja Principal", TipoCaja.CajaAhorro, BigDecimal.ZERO );

        doThrow(new MontoInvalidoException("El monto debe ser mayor a cero"))
                .when(cajaService)
                .depositar(eq(1L), any(BigDecimal.class));

        mockMvc.perform(put(BASE_URL + "/1/depositar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(cajaService).depositar(eq(1L), any(BigDecimal.class));
    }

    @Test
    void retirarSaldoInsuficiente() throws Exception {

        CajaInfoResponse request = new CajaInfoResponse(1L, 12345L, "Caja Principal", TipoCaja.CajaAhorro, BigDecimal.valueOf(10000) );

        doThrow(new SaldoInsuficienteException("Saldo insuficiente"))
                .when(cajaService)
                .retirar(eq(1L), any(BigDecimal.class));

        mockMvc.perform(put(BASE_URL + "/1/retirar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity());

        verify(cajaService).retirar(eq(1L), any(BigDecimal.class));
    }
}
