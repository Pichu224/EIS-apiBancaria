package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDAO transferenciaDAO;

    public TransferenciaServiceImpl(TransferenciaDAO transferenciaDAO){
        this.transferenciaDAO = transferenciaDAO;
    }

    @Override
    public void crear(Transferencia transferencia){

    }

    @Override
    public Transferencia recuperar(Long idTransferencia){
        return null;
    }

    @Override
    public void actualizar(Transferencia transferencia){

    }

    @Override
    public void eliminar(Long idTransferencia){

    }
}
