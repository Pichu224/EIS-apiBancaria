package com.unq.eis.apibancaria.controller.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long idMovimiento,
        BigDecimal monto,
        LocalDateTime fechaRealizado,
        String descripcion,
        Long nroTransferencia,
        Long idCaja
) {}
