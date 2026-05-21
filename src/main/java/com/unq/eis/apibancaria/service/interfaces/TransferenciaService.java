package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;

import java.math.BigDecimal;

public interface TransferenciaService {

    Transferencia tranferir(Long idCajaOrigen, Long idCajaDestino, BigDecimal montoTotal);
    Transferencia recuperar(Long idTransferencia);
}
