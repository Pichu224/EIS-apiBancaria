package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Caja;

public interface CajaService {
    void crear(Caja caja);
    Caja recuperar(Long idCaja);
    void actualizar(Caja caja);
    void eliminar(Long idCaja);
}
