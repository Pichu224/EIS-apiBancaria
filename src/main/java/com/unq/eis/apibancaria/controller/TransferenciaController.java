package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.controller.dto.response.CajaInfoResponse;
import com.unq.eis.apibancaria.controller.dto.response.TransferenciaResponse;
import com.unq.eis.apibancaria.controller.mapper.CajaMapper;
import com.unq.eis.apibancaria.controller.mapper.TransferenciaMapper;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.service.impl.TransferenciaServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transferencias")
@AllArgsConstructor
public class TransferenciaController {

    private final TransferenciaServiceImpl transferenciaService;

    @PostMapping("/{idCajaOrigen}/transferir")
    public ResponseEntity<Void> transferir(@PathVariable Long idCajaOrigen, @RequestBody CajaInfoResponse caja){
        transferenciaService.tranferir(idCajaOrigen, CajaMapper.aModeloAlias(caja), CajaMapper.aModeloMonto(caja));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idTransferencia}")
    public ResponseEntity<TransferenciaResponse> recuperar(@PathVariable Long idTransferencia){
        Transferencia transferenciaModelo = transferenciaService.recuperar(idTransferencia);
        TransferenciaResponse transferenciaResponse = TransferenciaMapper.desdeModelo(transferenciaModelo);
        return ResponseEntity.ok(transferenciaResponse);
    }
}
