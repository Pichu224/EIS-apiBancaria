package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final CajaDAO cajaDao;

    public CajaServiceImpl(CajaDAO cajaDao){

        this.cajaDao = cajaDao;
    }

    @Override
    public void crear(Caja caja){

    }

    @Override
    public Caja recuperar(Long idCaja){
        return null;
    }

    @Override
    public void actualizar(Caja caja){

    }

    @Override
    public void eliminar(Long idCaja){

    }
}
