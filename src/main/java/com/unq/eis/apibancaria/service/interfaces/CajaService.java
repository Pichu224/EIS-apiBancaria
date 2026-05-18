package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Caja;

public interface CajaService {

    Caja crear(Caja caja);
    Caja recuperar(Long idCaja);
    Caja actualizar(Long id, Caja caja);
    void eliminar(Long idCaja);
}
