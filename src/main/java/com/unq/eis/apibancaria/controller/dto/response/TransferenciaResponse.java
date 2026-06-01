package com.unq.eis.apibancaria.controller.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferenciaResponse(
        Long idTransferencia,
        LocalDateTime fechaRealizado,
        BigDecimal montoTotal,
        Long idCajaOrigen,
        Long idCajaDestino
) {
}
