package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Movimiento;

public interface MovimientoService {

    Movimiento crear(Movimiento movimiento);
    Movimiento recuperar(Long idMovimiento);
    Movimiento actualizar(Long id, Movimiento movimiento);
    void eliminar(Long idMovimiento);
}
