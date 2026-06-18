package com.unq.eis.apibancaria.controller.dto.request;

public record UsuarioActualizarRequest(
        String email,
        String contrasenia,
        String nombre,
        String apellido,
        String dni
) {}
