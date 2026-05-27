package com.unq.eis.apibancaria.controller.dto.request;

public record UsuarioCajaRequest(
        Long id,
        String email,
        String contrasenia
) {

}
