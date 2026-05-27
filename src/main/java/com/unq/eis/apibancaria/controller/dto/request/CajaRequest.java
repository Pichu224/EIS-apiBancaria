package com.unq.eis.apibancaria.controller.dto.request;

public record CajaRequest(
        Long nroCaja,
        String alias,
        UsuarioCajaRequest usuario) {}
