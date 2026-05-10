package com.unq.eis.apibancaria.service.interfaces;

import com.unq.eis.apibancaria.modelo.Transferencia;

public interface TransferenciaService {

    void crear(Transferencia transferencia);
    Transferencia recuperar(Long idTransferencia);
    void actualizar(Transferencia transferencia);
    void eliminar(Long idTransferencia);

}
