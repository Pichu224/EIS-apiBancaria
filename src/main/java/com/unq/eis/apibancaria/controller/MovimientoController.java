package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.controller.dto.response.MovimientoResponse;
import com.unq.eis.apibancaria.controller.mapper.MovimientoMapper;
import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.service.impl.MovimientoServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@AllArgsConstructor
public class MovimientoController {

    private final MovimientoServiceImpl movimientoService;

    @GetMapping("/{idCaja}")
    public ResponseEntity<List<MovimientoResponse>> recuperarMovimientosDeCaja(@PathVariable Long idCaja) {

        List<Movimiento> movimientos = movimientoService.recuperarMovimientosDeCaja(idCaja);
        List<MovimientoResponse> movimientosResponse = MovimientoMapper.desdeModelo(movimientos);
        return ResponseEntity.ok(movimientosResponse);
    }
}
