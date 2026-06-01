package com.unq.eis.apibancaria.controller.mapper;

import com.unq.eis.apibancaria.controller.dto.response.MovimientoResponse;
import com.unq.eis.apibancaria.modelo.Movimiento;

import java.util.List;

public class MovimientoMapper {

    public static MovimientoResponse desdeModelo(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getIdMovimiento(),
                movimiento.getMonto(),
                movimiento.getFechaRealizado(),
                movimiento.getDescripcion(),
                movimiento.getNroTransferencia(),
                movimiento.getCajaUtilizada().getIdCaja()
        );
    }

    public static List<MovimientoResponse> desdeModelo(List<Movimiento> movimientos) {
        return movimientos.stream()
                .map(MovimientoMapper::desdeModelo)
                .toList();
    }
}
