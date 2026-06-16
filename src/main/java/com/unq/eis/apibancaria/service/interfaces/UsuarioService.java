package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Usuario;

import java.math.BigDecimal;

public interface UsuarioService {
    Usuario crear(Usuario usuario);
    Usuario recuperar(Long idUsuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long idUsuario);
    Usuario login(String email, String contrasenia);
    Usuario register(String email, String contrasenia);
}
