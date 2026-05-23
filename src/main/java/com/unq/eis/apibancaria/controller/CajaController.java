package com.unq.eis.apibancaria.controller;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cajas")
@AllArgsConstructor
public class CajaController {

    private final CajaServiceImpl cajaService;

    @PostMapping
    public ResponseEntity<Caja> crear(@RequestBody Caja caja) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cajaService.crear(caja));
    }

    @GetMapping("/{idCaja}")
    public ResponseEntity<Caja> recuperar(@PathVariable Long idCaja) {
        return ResponseEntity.ok(cajaService.recuperar(idCaja));
    }

    @PutMapping("/{idCaja}")
    public ResponseEntity<Caja> actualizar(@PathVariable Long idCaja, @RequestBody Caja caja) {
        return ResponseEntity.ok(cajaService.actualizar(idCaja, caja));
    }

    @DeleteMapping("/{idCaja}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idCaja) {
        cajaService.eliminar(idCaja);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idCaja}/depositar")
    public ResponseEntity<Void> depositar(@PathVariable Long idCaja, @RequestBody BigDecimal monto) {
        cajaService.depositar(idCaja, monto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idCaja}/retirar")
    public ResponseEntity<Void> retirar(@PathVariable Long idCaja, @RequestBody BigDecimal monto) {
        cajaService.retirar(idCaja, monto);
        return ResponseEntity.ok().build();
    }
}