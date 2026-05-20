package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDAO transferenciaDAO;

    public TransferenciaServiceImpl(TransferenciaDAO transferenciaDAO) {
        this.transferenciaDAO = transferenciaDAO;
    }

    @Override
    public void crear(Transferencia transferencia) {
        transferenciaDAO.save(transferencia);
    }

    @Override
    public Transferencia recuperar(Long idTransferencia) {
        return transferenciaDAO.findById(idTransferencia)
                .orElseThrow(() -> new EntityNotFoundException("No existe la transferencia con id " + idTransferencia));
    }

    @Override
    public void actualizar(Transferencia transferencia) {
        if (transferencia.getIdTransferencia() == null || !transferenciaDAO.existsById(transferencia.getIdTransferencia())) {
            throw new EntityNotFoundException("No existe la transferencia con id " + transferencia.getIdTransferencia());
        }
        transferenciaDAO.save(transferencia);
    }

    @Override
    public void eliminar(Long idTransferencia) {
        if (!transferenciaDAO.existsById(idTransferencia)) {
            throw new EntityNotFoundException("No existe la transferencia con id " + idTransferencia);
        }
        transferenciaDAO.deleteById(idTransferencia);
    }
}
