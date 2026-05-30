package com.unq.eis.apibancaria.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unq.eis.apibancaria.controller.dto.request.UsuarioRequest;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    private static final String BASE_URL = "/usuarios";

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    @Test
    void obtenerUsuario_existente_retornOk() throws Exception {
        Usuario usuario = new Usuario(1L,"test@example.com", "secret");

        when(usuarioService.recuperar(1L)).thenReturn(usuario);

        mockMvc.perform(get(BASE_URL + "/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.contrasenia").value("secret"));
    }

    @Test
    void crearUsuario_exitoso_retornCreated() throws Exception {
        UsuarioRequest request = new UsuarioRequest("test@example.com", "secret");

        // Simular que el servicio asigna el id al usuario pasado por referencia
        doAnswer(invocation -> {
            Usuario arg = invocation.getArgument(0);
            arg.setIdUsuario(1L);
            return null; // crear() es void según el controller
        }).when(usuarioService).crear(any(Usuario.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.contrasenia").value("secret"));

        verify(usuarioService).crear(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_exitoso_retornOk() throws Exception {
        UsuarioRequest request = new UsuarioRequest("test@example.com", "secret");

        // Simular que actualizar() es void; solo verificamos que fue llamado y que el controller devuelve el modelo con id
        doAnswer(invocation -> {
            Usuario arg = invocation.getArgument(1);
            // aseguramos que tiene el id seteado por el controller
            arg.setIdUsuario(1L);
            return null;
        }).when(usuarioService).actualizar(eq(1L), any(Usuario.class));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.contrasenia").value("secret"));

        verify(usuarioService).actualizar(eq(1L), any(Usuario.class));
    }

    @Test
    void eliminarUsuario_exitoso_retornNoContent() throws Exception {
        // eliminar() es void por lo que basta con doNothing (por defecto no hace falta)
        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(1L);
    }
}

