package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Movimiento;

public interface MovimientoService {
    // Estos metodos vuelan y son otros, como por ejemplo recuperar todos los movimientos de una caja.
    Movimiento crear(Movimiento movimiento);
    Movimiento recuperar(Long idMovimiento);
}
