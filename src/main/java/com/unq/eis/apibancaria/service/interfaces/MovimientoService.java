package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Movimiento;

import java.util.List;

public interface MovimientoService {

    List<Movimiento> recuperarMovimientosDeCaja(Long idCaja);
}
