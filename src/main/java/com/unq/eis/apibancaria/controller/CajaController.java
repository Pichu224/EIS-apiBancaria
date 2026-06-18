package com.unq.eis.apibancaria.controller;
import com.unq.eis.apibancaria.controller.dto.request.CajaActualizarRequest;
import com.unq.eis.apibancaria.controller.dto.request.CajaRequest;
import com.unq.eis.apibancaria.controller.dto.response.CajaInfoResponse;
import com.unq.eis.apibancaria.controller.dto.response.CajaResponse;
import com.unq.eis.apibancaria.controller.mapper.CajaMapper;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cajas")
@AllArgsConstructor
public class CajaController {

    private final CajaServiceImpl cajaService;

    @PostMapping
    public ResponseEntity<CajaResponse> crear(@RequestBody CajaRequest caja) {
        Caja cajaModelo = CajaMapper.aModelo(caja);
        this.cajaService.crear(cajaModelo);
        CajaResponse cajaResponse = CajaMapper.desdeModelo(cajaModelo);
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaResponse);
    }

    @GetMapping("/{idCaja}")
    public ResponseEntity<CajaResponse> recuperar(@PathVariable Long idCaja) {
        Caja cajaModelo = cajaService.recuperar(idCaja);
        CajaResponse cajaResponse = CajaMapper.desdeModelo(cajaModelo);
        return ResponseEntity.ok(cajaResponse);
    }

    @PutMapping("/{idCaja}")
    public ResponseEntity<CajaInfoResponse> actualizar(@PathVariable Long idCaja, @RequestBody CajaActualizarRequest caja) {
        Caja cajaActual = CajaMapper.aModelo(caja);
        cajaActual.setIdCaja(idCaja);
        CajaInfoResponse cajaResponse = CajaMapper.desdeModeloInfo(cajaService.actualizar(idCaja, cajaActual));
        return ResponseEntity.ok(cajaResponse);
    }

    @DeleteMapping("/{idCaja}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idCaja) {
        cajaService.eliminar(idCaja);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{idCaja}/depositar")
    public ResponseEntity<Void> depositar(@PathVariable Long idCaja, @RequestBody CajaInfoResponse caja) {
        cajaService.depositar(idCaja, CajaMapper.aModeloMonto(caja));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idCaja}/retirar")
    public ResponseEntity<Void> retirar(@PathVariable Long idCaja, @RequestBody CajaInfoResponse caja) {
        cajaService.retirar(idCaja, CajaMapper.aModeloMonto(caja));
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{idUsuario}/cajas")
    public ResponseEntity<List<CajaInfoResponse>> recuperarCajasdeUsuario(@PathVariable Long idUsuario){
        List<Caja> cajasModelo = cajaService.recuperarCajasdeUsuario(idUsuario);
        List<CajaInfoResponse> responsesCajas = CajaMapper.desdeModeloCajas(cajasModelo);
        return ResponseEntity.ok(responsesCajas);
    }

}