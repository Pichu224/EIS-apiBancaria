package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Caja;

import java.math.BigDecimal;
import java.util.List;

public interface CajaService {

    Caja crear(Caja caja);
    Caja recuperar(Long idCaja);
    Caja actualizar(Long id, Caja caja);
    void eliminar(Long idCaja);
    void depositar(Long idCaja, BigDecimal monto);
    void retirar(Long idCaja, BigDecimal monto);
    List<Caja> recuperarCajasdeUsuario(Long idUsuario);
}
