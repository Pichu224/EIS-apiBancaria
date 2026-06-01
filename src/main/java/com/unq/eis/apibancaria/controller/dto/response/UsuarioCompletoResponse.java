package com.unq.eis.apibancaria.controller.dto.response;

import java.time.LocalDateTime;

public record UsuarioCompletoResponse(
        Long id,
        String email,
        String contrasenia,
        String nombre,
        String apellido,
        String dni,
        LocalDateTime fechaRegistro
) {}
