package com.unq.eis.apibancaria.controller.mapper;

import com.unq.eis.apibancaria.controller.dto.request.UsuarioRequest;
import com.unq.eis.apibancaria.controller.dto.response.UsuarioResponse;
import com.unq.eis.apibancaria.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    static public Usuario aModelo(UsuarioRequest request) {
        return new Usuario(
                request.email(),
                request.contrasenia()
        );
    }

    static public UsuarioResponse desdeModelo(Usuario modelo) {
        return new UsuarioResponse(
                modelo.getIdUsuario(),
                modelo.getEmail(),
                modelo.getContrasenia()
        );
    }

    static public List<UsuarioResponse> todosDesdeModelo(List<Usuario> modelos) {
        List<UsuarioResponse> resp = new ArrayList<>();

        for (Usuario modelo : modelos) {
            resp.add(desdeModelo(modelo));
        }
        return resp;
    }
}
