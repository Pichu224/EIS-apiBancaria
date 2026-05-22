package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.controller.dto.request.UsuarioRequest;
import com.unq.eis.apibancaria.controller.dto.response.UsuarioResponse;
import com.unq.eis.apibancaria.controller.mapper.UserMapper;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuario(@PathVariable Long id){
        Usuario usuario = this.usuarioService.recuperar(id);
        UsuarioResponse usuarioResponse = UserMapper.desdeModelo(usuario);
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
    public ResponseEntity<UsuarioResponse> eliminarUsuario(@PathVariable Long id){
        this.usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(@Valid @PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest){
        Usuario usuario = UserMapper.aModelo(usuarioRequest);
        usuario.setIdUsuario(id);
        this.usuarioService.actualizar(id, usuario);
        UsuarioResponse usuarioResponse = UserMapper.desdeModelo(usuario);
        return ResponseEntity.ok(usuarioResponse);
    }
}
