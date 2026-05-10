package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.exception.*;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.UsuarioService;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
    public void crear(Usuario usuario){

        if(this.usuarioDao.existsByEmail(usuario.getEmail()))
            throw new EmailYaExistenteException("Ya existe un usuario con el mail ingresado, por favor ingresar uno distinto!");
        this.usuarioDao.save(usuario);

    }

    @Override
    public Usuario recuperar(Long idUsuario){

        try {
            return this.usuarioDao.findById(idUsuario).orElseThrow(
                    () -> new UsuarioInexistenteException("No existe ningun usuario persistido con ese id!"));
        } catch (InvalidDataAccessApiUsageException e) {
            throw new UsuarioInexistenteException("El usuario no posee id!");
        }

    }
    @Override
    public void actualizar(Usuario usuario){

        try{
            Usuario usuarioRecuperado = this.usuarioDao.findById(usuario.getIdUsuario()).orElseThrow(
                    () -> new UsuarioInexistenteException("No existe ningun usuario persistido con ese id!"));

            usuario.setIdUsuario(usuarioRecuperado.getIdUsuario());

            this.usuarioDao.save(usuario);

        }catch (InvalidDataAccessApiUsageException e){
            throw new UsuarioInexistenteException("El usuario no posee id!");
        }

    }
    @Override
    public void eliminar(Long idUsuario){

        try {
            this.usuarioDao.deleteById(idUsuario);
        } catch (InvalidDataAccessApiUsageException e) {
            throw new UsuarioInexistenteException("El usuario no posee id!");
        }

    }

}
