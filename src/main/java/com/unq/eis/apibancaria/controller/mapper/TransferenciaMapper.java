package com.unq.eis.apibancaria.controller.mapper;

import com.unq.eis.apibancaria.controller.dto.response.TransferenciaResponse;
import com.unq.eis.apibancaria.modelo.Transferencia;

public class TransferenciaMapper {

    static public TransferenciaResponse desdeModelo(Transferencia modelo){
        return new TransferenciaResponse(
                modelo.getIdTransferencia(),
                modelo.getFechaRealizado(),
                modelo.getMontoTotal(),
                modelo.getCajaOrigen().getIdCaja(),
                modelo.getCajaDestino().getIdCaja()
        );
    }
}
