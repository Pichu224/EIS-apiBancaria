package com.unq.eis.apibancaria.controller.dto.response;

public record UsuarioResponse(
        Long id,
        String email,
        String contrasenia,
        Integer saldo
) {}
