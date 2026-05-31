package com.unq.eis.apibancaria.service.impl;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.IdNuloException;
import com.unq.eis.apibancaria.exception.TransferenciaInexistenteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
@AllArgsConstructor
public class TransferenciaServiceImpl implements TransferenciaService {

    private final TransferenciaDAO transferenciaDao;
    private final CajaDAO cajaDao;
    private final MovimientoDAO movimientoDao;

    private Caja recuperarCajaDeBD(Long idCaja){
        return cajaDao.findById(idCaja).orElseThrow(() -> new CajaInexistenteException("Caja no encontrada!"));
    }

    public Transferencia tranferir(Long idCajaOrigen, Long idCajaDestino, BigDecimal montoTotal){
        Caja cajaOrigen = recuperarCajaDeBD(idCajaOrigen);
        Caja cajaDestino = recuperarCajaDeBD(idCajaDestino);

        cajaOrigen.retirar(montoTotal);
        cajaDestino.depositar(montoTotal);

        Transferencia transferencia = new Transferencia(montoTotal, cajaOrigen, cajaDestino);
        transferenciaDao.save(transferencia);

        Movimiento movimiento = new Movimiento(transferencia.getIdTransferencia(), cajaOrigen, montoTotal, cajaDestino.getNroCaja().toString());
        movimientoDao.save(movimiento);

        return transferencia;
    }

    @Transactional(readOnly = true)
    public Transferencia recuperar(Long idTransferencia){
        return transferenciaDao.findById(idTransferencia)
                .orElseThrow(() -> new TransferenciaInexistenteException("Transferencia no encontrada"));
    }
}
