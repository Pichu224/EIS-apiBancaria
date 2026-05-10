package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Movimiento;

public interface MovimientoService {

    void crear(Movimiento movimiento);
    Movimiento recuperar(Long idMovimiento);
    void actualizar(Movimiento movimiento);
    void eliminar(Long idMovimiento);
}
