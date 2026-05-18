package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDAO transferenciaDAO;

    @Override
    public Transferencia crear(Transferencia transferencia){

    }

    @Override
    public Transferencia recuperar(Long idTransferencia){
        return null;
    }

    @Override
    public Transferencia actualizar(Long id, Transferencia transferencia){

    }

    @Override
    public void eliminar(Long idTransferencia){

    }
}
