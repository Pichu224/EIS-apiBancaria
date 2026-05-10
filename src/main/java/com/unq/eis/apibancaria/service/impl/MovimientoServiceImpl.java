package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoDAO movimientoDAO;

    public MovimientoServiceImpl(MovimientoDAO movimientoDAO){
        this.movimientoDAO = movimientoDAO;
    }

    @Override
    public void crear(Movimiento movimiento){

    }
    @Override
    public Movimiento recuperar(Long idMovimiento){
        return null;
    }
    @Override
    public void actualizar(Movimiento movimiento){

    }
    @Override
    public void eliminar(Long idMovimiento){

    }
}
