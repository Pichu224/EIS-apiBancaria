package com.unq.eis.apibancaria.controller.dto.response;

import com.unq.eis.apibancaria.modelo.TipoCaja;

import java.math.BigDecimal;

public record CajaInfoResponse(
        Long id,
        Long nroCaja,
        String alias,
        TipoCaja tipoCaja,
        BigDecimal saldo
) {}
