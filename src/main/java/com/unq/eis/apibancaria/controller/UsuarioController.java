package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.controller.dto.request.UsuarioActualizarRequest;
import com.unq.eis.apibancaria.controller.dto.request.UsuarioRequest;
import com.unq.eis.apibancaria.controller.dto.response.UsuarioCompletoResponse;
import com.unq.eis.apibancaria.controller.dto.response.UsuarioResponse;
import com.unq.eis.apibancaria.controller.mapper.UserMapper;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponse> obtenerUsuario(@PathVariable Long id){
        Usuario usuario = this.usuarioService.recuperar(id);
        UsuarioCompletoResponse usuarioResponse = UserMapper.desdeModeloCompleto(usuario);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crearUsuario(@RequestBody UsuarioRequest usuarioRequest){
        Usuario usuario = UserMapper.aModelo(usuarioRequest);
        this.usuarioService.crear(usuario);
        UsuarioResponse usuarioResponse = UserMapper.desdeModelo(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        this.usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponse> actualizarUsuario(@Valid @PathVariable Long id, @RequestBody UsuarioActualizarRequest usuarioRequest){
        Usuario usuario = UserMapper.aModelo(usuarioRequest);
        this.usuarioService.actualizar(id, usuario);
        UsuarioCompletoResponse usuarioResponse = UserMapper.desdeModeloCompleto(usuario);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioCompletoResponse> login(@RequestBody UsuarioRequest usuarioRequest){
        Usuario usuario = usuarioService.login(usuarioRequest.email(), usuarioRequest.contrasenia());
        UsuarioCompletoResponse usuarioResponse = UserMapper.desdeModeloCompleto(usuario);
        return ResponseEntity.ok(usuarioResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioCompletoResponse> register(@RequestBody UsuarioRequest usuarioRequest){
        Usuario usuario = usuarioService.register(usuarioRequest.email(), usuarioRequest.contrasenia());
        UsuarioCompletoResponse usuarioResponse = UserMapper.desdeModeloCompleto(usuario);
        return ResponseEntity.ok(usuarioResponse);
    }
}
