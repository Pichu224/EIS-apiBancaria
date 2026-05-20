package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoDAO movimientoDAO;

    public MovimientoServiceImpl(MovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    @Override
    public void crear(Movimiento movimiento) {
        movimientoDAO.save(movimiento);
    }

    @Override
    public Movimiento recuperar(Long idMovimiento) {
        return movimientoDAO.findById(idMovimiento)
                .orElseThrow(() -> new EntityNotFoundException("No existe el movimiento con id " + idMovimiento));
    }

    @Override
    public void actualizar(Movimiento movimiento) {
        if (movimiento.getIdMovimiento() == null || !movimientoDAO.existsById(movimiento.getIdMovimiento())) {
            throw new EntityNotFoundException("No existe el movimiento con id " + movimiento.getIdMovimiento());
        }
        movimientoDAO.save(movimiento);
    }

    @Override
    public void eliminar(Long idMovimiento) {
        if (!movimientoDAO.existsById(idMovimiento)) {
            throw new EntityNotFoundException("No existe el movimiento con id " + idMovimiento);
        }
        movimientoDAO.deleteById(idMovimiento);
    }
}
