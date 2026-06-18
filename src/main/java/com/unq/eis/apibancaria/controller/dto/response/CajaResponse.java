package com.unq.eis.apibancaria.controller.dto.response;

import com.unq.eis.apibancaria.modelo.TipoCaja;

import java.math.BigDecimal;

public record CajaResponse(
        Long id,
        Long nroCaja,
        String alias,
        TipoCaja tipoCaja,
        BigDecimal saldo,
        UsuarioResponse usuario
) {}
