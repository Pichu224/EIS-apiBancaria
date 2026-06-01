package com.unq.eis.apibancaria.controller.mapper;

import com.unq.eis.apibancaria.controller.dto.request.CajaActualizarRequest;
import com.unq.eis.apibancaria.controller.dto.request.CajaRequest;
import com.unq.eis.apibancaria.controller.dto.response.CajaInfoResponse;
import com.unq.eis.apibancaria.controller.dto.response.CajaResponse;
import com.unq.eis.apibancaria.modelo.Caja;

import java.math.BigDecimal;

public class CajaMapper {

    static public Caja aModelo(CajaRequest request) {
        return new Caja(
                request.nroCaja(),
                request.alias(),
                UserMapper.aModeloParaCaja(request.usuario())
        );
    }
    static public Caja aModelo(CajaActualizarRequest request) {
        return new Caja(
                request.nroCaja(),
                request.alias(),
                request.tipoCaja()
        );
    }

    static public CajaResponse desdeModelo(Caja modelo) {
        return new CajaResponse(
                modelo.getIdCaja(),
                modelo.getNroCaja(),
                modelo.getAlias(),
                modelo.getTipoCaja(),
                modelo.getSaldo(),
                UserMapper.desdeModelo(modelo.getUsuario())
        );
    }

    static public CajaInfoResponse desdeModeloInfo(Caja modelo){
        return new CajaInfoResponse(
                modelo.getIdCaja(),
                modelo.getNroCaja(),
                modelo.getAlias(),
                modelo.getTipoCaja(),
                modelo.getSaldo()
        );
    }

    static public BigDecimal aModeloMonto(CajaInfoResponse response) {
        return response.saldo();
    }
}
