package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.IdNuloException;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDao;

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuarioDao.existsByEmail(usuario.getEmail())) {
            throw new EmailYaExistenteException("Ya existe un usuario con ese email");
        }
        return usuarioDao.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario recuperar(Long idUsuario) {
        this.validarIdUsuario(idUsuario);
        return usuarioDao.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {

        this.validarIdUsuario(id);

        Usuario existente = usuarioDao.findById(id)
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado"));

        // Validar email si cambió
        if (!existente.getEmail().equals(usuario.getEmail()) &&
                usuarioDao.existsByEmail(usuario.getEmail())) {

            throw new EmailYaExistenteException("Ya existe un usuario con ese email");
        }

        existente.setEmail(usuario.getEmail());
        existente.setContrasenia(usuario.getContrasenia());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setDni(usuario.getDni());

        return existente;
    }

    @Override
    public void eliminar(Long idUsuario) {
        if (idUsuario == null || !usuarioDao.existsById(idUsuario)) {
            throw new UsuarioInexistenteException("Usuario no encontrado");
        }
        usuarioDao.deleteById(idUsuario);
    }

    private void validarIdUsuario(Long idUsuario){
        if (idUsuario == null) {
            throw new IdNuloException("El id no puede ser null");
        }
    }
}