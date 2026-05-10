package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Usuario;

public interface UsuarioService {
    void crear(Usuario usuario);
    Usuario recuperar(Long idUsuario);
    void actualizar(Usuario usuario);
    void eliminar(Long idUsuario);
}
