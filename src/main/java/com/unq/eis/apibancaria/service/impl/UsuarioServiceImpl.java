package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDao;

    public UsuarioServiceImpl(UsuarioDAO usuarioDao) {
        this.usuarioDao = usuarioDao;
    }

    @Override
    public void crear(Usuario usuario) {

        if (usuarioDao.existsByEmail(usuario.getEmail())) {
            throw new EmailYaExistenteException("Ya existe un usuario con ese email");
        }

        usuarioDao.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario recuperar(Long idUsuario) {

        if (idUsuario == null) {
            throw new UsuarioInexistenteException("El id no puede ser null");
        }

        return usuarioDao.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado"));
    }

    @Override
    public void actualizar(Usuario usuario) {

        if (usuario.getIdUsuario() == null) {
            throw new UsuarioInexistenteException("El usuario no tiene id");
        }

        Usuario existente = usuarioDao.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado"));

        // Validar email si cambió
        if (!existente.getEmail().equals(usuario.getEmail()) &&
                usuarioDao.existsByEmail(usuario.getEmail())) {

            throw new EmailYaExistenteException("Ya existe un usuario con ese email");
        }

        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setDni(usuario.getDni());
        existente.setContrasenia(usuario.getContrasenia());
        existente.setApellido(usuario.getApellido());
        existente.setSaldo(usuario.getSaldo());

        usuarioDao.save(existente);
    }

    @Override
    public void eliminar(Long idUsuario) {

        if (idUsuario == null || !usuarioDao.existsById(idUsuario)) {
            throw new UsuarioInexistenteException("Usuario no encontrado");
        }

        usuarioDao.deleteById(idUsuario);
    }

    public Integer consultarSaldo(Long idUsuario){
        if (idUsuario == null || !usuarioDao.existsById(idUsuario)) {
            throw new UsuarioInexistenteException("Usuario no encontrado");
        }

        return usuarioDao.findById(idUsuario).get().getSaldo();
    }
}