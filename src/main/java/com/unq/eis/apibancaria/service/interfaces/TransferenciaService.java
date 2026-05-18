package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Transferencia;

public interface TransferenciaService {

    Transferencia crear(Transferencia transferencia);
    Transferencia recuperar(Long idTransferencia);
    Transferencia actualizar(Long id, Transferencia transferencia);
    void eliminar(Long idTransferencia);

}
