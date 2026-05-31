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

    @PostMapping("/{idCajaOrigen}/transferir/{idCajaDestino}")
    public ResponseEntity<Void> transferir(@PathVariable Long idCajaOrigen, @PathVariable Long idCajaDestino, @RequestBody CajaInfoResponse caja){
        // Lo deje para la prueba la 'caja', se puede cambiar si hacer un dto particular para el monto o no.
        transferenciaService.tranferir(idCajaOrigen, idCajaDestino, CajaMapper.aModeloMonto(caja));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{idTransferencia}")
    public ResponseEntity<TransferenciaResponse> recuperar(@PathVariable Long idTransferencia){
        Transferencia transferenciaModelo = transferenciaService.recuperar(idTransferencia);
        TransferenciaResponse transferenciaResponse = TransferenciaMapper.desdeModelo(transferenciaModelo);
        return ResponseEntity.ok(transferenciaResponse);
    }
}
