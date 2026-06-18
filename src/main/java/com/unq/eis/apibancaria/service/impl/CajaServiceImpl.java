package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.*;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

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
        return cajaDao.findById(idCaja)
                .orElseThrow(() -> new CajaInexistenteException("Caja no encontrada!"));
    }

    @Override
    public Caja actualizar(Long idCaja, Caja caja){ // Solo se puede actualizar el nroCaja, Alias y TipodeCaja.
        Caja cajaRecuperada = this.recuperar(idCaja);

        if(!(cajaRecuperada.getNroCaja().equals(caja.getNroCaja())) &&
                cajaDao.existsByNroCaja(caja.getNroCaja()))
            throw new NroCajaYaExistenteException("Ya existe una caja con ese número asignado.");

        if(!(cajaRecuperada.getAlias().equals(caja.getAlias())) && cajaDao.existsByAlias(caja.getAlias()))
            throw new AliasYaExistenteException("Ya existe una caja con ese alias.");

        cajaRecuperada.setNroCaja(caja.getNroCaja());
        cajaRecuperada.setAlias(caja.getAlias());
        cajaRecuperada.setTipoCaja(caja.getTipoCaja());

        return cajaRecuperada;
    }

    @Override
    public void eliminar(Long idCaja){
        this.recuperar(idCaja);
        cajaDao.deleteById(idCaja);
    }

    @Override
    public void depositar(Long idCaja, BigDecimal monto){
        Caja cajaDepositar = this.recuperar(idCaja);
        cajaDepositar.depositar(monto);
    }

    @Override
    public void retirar(Long idCaja, BigDecimal monto){
        Caja cajaRetirar = this.recuperar(idCaja);
        cajaRetirar.retirar(monto);
    }

    private void validarCreacionCaja(@NonNull Caja caja){
        if(!usuarioDAO.existsById(caja.getUsuario().getIdUsuario()))
            throw new UsuarioInexistenteException("El usuario no existe!");

        if(cajaDao.existsByNroCaja(caja.getNroCaja()))
            throw new NroCajaYaExistenteException("Ya existe el numero de Caja elegido");

        if(cajaDao.existsByAlias(caja.getAlias()))
            throw new AliasYaExistenteException("Ya existe el alias ingresado");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Caja> recuperarCajasdeUsuario(Long idUsuario){

        if(!usuarioDAO.existsById(idUsuario)) {
            throw new UsuarioInexistenteException("El usuario no existe!");
        }
        return cajaDao.findByUsuario_IdUsuario(idUsuario);
    }
}
