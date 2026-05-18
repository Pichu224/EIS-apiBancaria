package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoDAO movimientoDAO;

    @Override
    public Movimiento crear(Movimiento movimiento){

    }
    @Override
    public Movimiento recuperar(Long idMovimiento){
        return null;
    }
    @Override
    public Movimiento actualizar(Long id, Movimiento movimiento){

    }
    @Override
    public void eliminar(Long idMovimiento){

    }
}
