package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoDAO movimientoDAO;
    private final CajaDAO cajaDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Movimiento> recuperarMovimientosDeCaja(Long idCaja) {

        if (!cajaDAO.existsById(idCaja)) {
            throw new CajaInexistenteException("Caja no Encontrada!");
        }

        return movimientoDAO.findByCajaUtilizada_IdCaja(idCaja);
    }

}
