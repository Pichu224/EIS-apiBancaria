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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@AllArgsConstructor
public class CajaServiceImpl implements CajaService {

    private final CajaDAO cajaDao;
    private final UsuarioDAO usuarioDAO;

    @Override
    public Caja crear(Caja caja){
        this.validarCreacionCaja(caja);
        return cajaDao.save(caja);
    }

    @Override
    public Caja recuperar(Long idCaja){
        this.validarIdCaja(idCaja);
        return this.recuperarCajaDeBD(idCaja);
    }

    @Override
    public Caja actualizar(Long idCaja, Caja caja){ // Solo se puede actualizar el nroCaja, Alias y TipodeCaja.

        this.validarIdCaja(idCaja);

        Caja cajaRecuperada = this.recuperarCajaDeBD(idCaja);

        if(!(cajaRecuperada.getNroCaja().equals(caja.getNroCaja())) && cajaDao.existsByNroCaja(caja.getNroCaja())){
            throw new NroCajaYaExistenteException("Ya existe una caja con ese número asignado.");
        }

        if(!(cajaRecuperada.getAlias().equals(caja.getAlias())) && cajaDao.existsByAlias(caja.getAlias())){
            throw new AliasYaExistenteException("Ya existe una caja con ese alias.");
        }

        cajaRecuperada.setNroCaja(caja.getNroCaja());
        cajaRecuperada.setAlias(caja.getAlias());
        cajaRecuperada.setTipoCaja(caja.getTipoCaja());

        return cajaRecuperada;

    }

    @Override
    public void eliminar(Long idCaja){
        if (idCaja == null || !cajaDao.existsById(idCaja)){
            throw new CajaInexistenteException("Caja no encontrada");
        }
        cajaDao.deleteById(idCaja);
    }

    @Override
    public void depositar(Long idCaja, BigDecimal monto){

        this.validarIdCaja(idCaja);

        Caja cajaDepositar = this.recuperarCajaDeBD(idCaja);

        cajaDepositar.depositar(monto);

    }

    @Override
    public void retirar(Long idCaja, BigDecimal monto){

        this.validarIdCaja(idCaja);

        Caja cajaRetirar = this.recuperarCajaDeBD(idCaja);

        cajaRetirar.retirar(monto);
    }

    private void validarCreacionCaja(Caja caja){
        Usuario usuarioCaja =  caja.getUsuario();

        if(usuarioCaja.getIdUsuario() == null ){
            throw new UsuarioInexistenteException("El id no puede ser null");
        }
        if(!usuarioDAO.existsById(usuarioCaja.getIdUsuario())){
            throw new UsuarioInexistenteException("El usuario no existe!");
        }
        if(cajaDao.existsByNroCaja(caja.getNroCaja())){
            throw new NroCajaYaExistenteException("Ya existe el numero de Caja elegido");
        }
        if(cajaDao.existsByAlias(caja.getAlias())){
            throw new AliasYaExistenteException("Ya existe el alias ingresado");
        }
    }

    private void validarIdCaja(Long id){
        if ( id == null) {
            throw new CajaInexistenteException("El id no puede ser null");
        }
    }

    private Caja recuperarCajaDeBD(Long idCaja){
        return cajaDao.findById(idCaja).orElseThrow(() -> new CajaInexistenteException("Caja no encontrada!"));
    }
}
