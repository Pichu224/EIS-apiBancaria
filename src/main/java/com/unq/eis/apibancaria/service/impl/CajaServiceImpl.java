package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.AliasYaExistenteException;
import com.unq.eis.apibancaria.exception.NroCajaYaExistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final CajaDAO cajaDao;
    private final UsuarioDAO usuarioDAO;

    public CajaServiceImpl(CajaDAO cajaDao, UsuarioDAO usuarioDAO){

        this.cajaDao = cajaDao;
        this.usuarioDAO = usuarioDAO;
    }

    @Override
    public void crear(Caja caja){

        Usuario usuarioCaja =  caja.getUsuario();

        if (usuarioCaja.getIdUsuario() == null ){
            throw new UsuarioInexistenteException("El id no puede ser null");
        }

        if( ! usuarioDAO.existsById(usuarioCaja.getIdUsuario())){
            throw new UsuarioInexistenteException("El usuario no existe!");
        }

        if( cajaDao.existsByNroCaja(caja.getNroCaja())){
            throw new NroCajaYaExistenteException("Ya existe el numero de Caja elegido");
        }

        if( cajaDao.existsByAlias(caja.getAlias())){
            throw new AliasYaExistenteException("Ya existe el alias ingresado");
        }

        cajaDao.save(caja);
    }

    @Override
    public Caja recuperar(Long idCaja){

        if (idCaja == null) {
            throw new CajaInexistenteException("El id no puede ser null");
        }

        return cajaDao.findById(idCaja).orElseThrow(() -> new CajaInexistenteException("Caja no encontrada!"));
    }

    @Override
    public void actualizar(Caja caja){ // Solo se puede actualizar el nroCaja, Alias y TipodeCaja.

        if(caja.getIdCaja() == null){
            throw new CajaInexistenteException("La caja no posee id");
        }

        Caja cajaRecuperada = cajaDao.findById(caja.getIdCaja()).orElseThrow( () -> new CajaInexistenteException("Caja no encontrada."));

        if(!(cajaRecuperada.getNroCaja().equals(caja.getNroCaja())) && cajaDao.existsByNroCaja(caja.getNroCaja())){
            throw new NroCajaYaExistenteException("Ya existe una caja con ese número asignado.");
        }

        if(!(cajaRecuperada.getAlias().equals(caja.getAlias())) && cajaDao.existsByAlias(caja.getAlias())){
            throw new AliasYaExistenteException("Ya existe una caja con ese alias.");
        }

        cajaRecuperada.setNroCaja(caja.getNroCaja());
        cajaRecuperada.setAlias(caja.getAlias());
        cajaRecuperada.setTipoCaja(caja.getTipoCaja());

    }

    @Override
    public void eliminar(Long idCaja){

        if (idCaja == null || !cajaDao.existsById(idCaja)){
            throw new CajaInexistenteException("Caja no encontrada");
        }
    }
}
