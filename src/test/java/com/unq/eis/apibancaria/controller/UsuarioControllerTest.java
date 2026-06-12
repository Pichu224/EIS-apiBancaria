package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.controller.dto.request.UsuarioActualizarRequest;
import com.unq.eis.apibancaria.exception.ContraseniaVaciaException;
import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.MailInvalidoException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
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
        mockMvc = MockMvcBuilders
                .standaloneSetup(usuarioController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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


        doAnswer(invocation -> {
            Usuario arg = invocation.getArgument(0);
            arg.setIdUsuario(1L);
            return null;
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


        doAnswer(invocation -> {
            Usuario arg = invocation.getArgument(1);
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

        doNothing().when(usuarioService).eliminar(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminar(1L);
    }

    @Test
    void loginExitoso() throws Exception {

        UsuarioRequest request =  new UsuarioRequest("test@test.com","secret");

        Usuario usuario = new Usuario(
                "test@test.com",
                "secret",
                "Nico",
                "Vaccaro",
                "12345678"
        );

        usuario.setIdUsuario(1L);

        when(usuarioService.login("test@test.com","secret")).thenReturn(usuario);

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.nombre").value("Nico"))
                .andExpect(jsonPath("$.apellido").value("Vaccaro"))
                .andExpect(jsonPath("$.dni").value("12345678"));

        verify(usuarioService).login("test@test.com","secret");
    }

    @Test
    void loginConCredencialesIncorrectas() throws Exception {

        UsuarioRequest request = new UsuarioRequest("test@test.com","incorrecta");

        when(usuarioService.login("test@test.com","incorrecta")).thenThrow(new UsuarioInexistenteException("Email o contraseña incorrectos"));

        mockMvc.perform(post(BASE_URL + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());

        verify(usuarioService).login("test@test.com", "incorrecta");
    }
    @Test
    void crearUsuarioConMailInvalido() throws Exception {

        UsuarioRequest request = new UsuarioRequest(
                "mailInvalido",
                "1234"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("El mail es vacío o es inválido!"));

        verify(usuarioService, never()).crear(any(Usuario.class));
    }
    @Test
    void crearUsuarioConContraseniaVacia() throws Exception {

        UsuarioRequest request = new UsuarioRequest(
                "test@test.com",
                ""
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("La contraseña tiene que tener al menos 4 carácteres!"));

        verify(usuarioService, never()).crear(any(Usuario.class));
    }
    @Test
    void crearUsuarioConContraseniaLongitudMenor() throws Exception {

        UsuarioRequest request = new UsuarioRequest(
                "test@test.com",
                "ab"
        );

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("La contraseña tiene que tener al menos 4 carácteres!"));

        verify(usuarioService, never()).crear(any(Usuario.class));
    }

    @Test
    void actualizarUsuarioConEmailYaExistente() throws Exception {

        UsuarioActualizarRequest request = new UsuarioActualizarRequest(
                "existentemail@test.com",
                "1234",
                "Nico",
                "Vaccaro",
                "12345678"
        );

        doThrow(new EmailYaExistenteException(
                "Ya existe un usuario con ese email"))
                .when(usuarioService)
                .actualizar(eq(1L), any(Usuario.class));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Ya existe un usuario con ese email"));

        verify(usuarioService).actualizar(eq(1L), any(Usuario.class));
    }
}

