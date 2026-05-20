package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final CajaDAO cajaDao;

    public CajaServiceImpl(CajaDAO cajaDao) {
        this.cajaDao = cajaDao;
    }

    @Override
    public void crear(Caja caja) {
        cajaDao.save(caja);
    }

    @Override
    public Caja recuperar(Long idCaja) {
        return cajaDao.findById(idCaja)
                .orElseThrow(() -> new EntityNotFoundException("No existe la caja con id " + idCaja));
    }

    @Override
    public void actualizar(Caja caja) {
        if (caja.getIdCaja() == null || !cajaDao.existsById(caja.getIdCaja())) {
            throw new EntityNotFoundException("No existe la caja con id " + caja.getIdCaja());
        }
        cajaDao.save(caja);
    }

    @Override
    public void eliminar(Long idCaja) {
        if (!cajaDao.existsById(idCaja)) {
            throw new EntityNotFoundException("No existe la caja con id " + idCaja);
        }
        cajaDao.deleteById(idCaja);
    }
}
