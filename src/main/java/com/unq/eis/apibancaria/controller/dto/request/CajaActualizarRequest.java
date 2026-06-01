package com.unq.eis.apibancaria.controller.dto.request;

import com.unq.eis.apibancaria.modelo.TipoCaja;

public record CajaActualizarRequest(
        Long nroCaja,
        String alias,
        TipoCaja tipoCaja
) {}
