package com.unq.eis.apibancaria.controller.mapper;

import com.unq.eis.apibancaria.controller.dto.request.CajaRequest;
import com.unq.eis.apibancaria.controller.dto.request.UsuarioRequest;
import com.unq.eis.apibancaria.controller.dto.response.CajaResponse;
import com.unq.eis.apibancaria.controller.dto.response.UsuarioResponse;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Usuario;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CajaMapper {

    static public Caja aModelo(CajaRequest request) {
        return new Caja(
                request.nroCaja(),
                request.alias(),
                UserMapper.aModeloParaCaja(request.usuario())
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
    static public BigDecimal aModeloMonto(CajaResponse response) {
        return response.saldo();
    }
}
