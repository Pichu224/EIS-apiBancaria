package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.exception.EmailYaExistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDao;
    private final CajaDAO cajaDAO;

    @Override
    public Usuario crear(Usuario usuario) {
        if (usuarioDao.existsByEmail(usuario.getEmail()))
            throw new EmailYaExistenteException("Ya existe un usuario con ese email");
        return usuarioDao.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario recuperar(Long idUsuario) {
        return usuarioDao.findById(idUsuario)
                .orElseThrow(() -> new UsuarioInexistenteException("Usuario no encontrado"));
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = this.recuperar(id);

        if (!existente.getEmail().equals(usuario.getEmail()) &&
                usuarioDao.existsByEmail(usuario.getEmail()))
            throw new EmailYaExistenteException("Ya existe un usuario con ese email");

        existente.setEmail(usuario.getEmail());
        existente.setContrasenia(usuario.getContrasenia());
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setDni(usuario.getDni());

        return existente;
    }

    @Override
    public void eliminar(Long idUsuario) {
        this.recuperar(idUsuario);
        usuarioDao.deleteById(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario login(String email, String contrasenia){
        return usuarioDao.findByEmailAndContrasenia(email,contrasenia)
                .orElseThrow(() ->  new UsuarioInexistenteException("Email o contraseña incorrectos"));
    }
}