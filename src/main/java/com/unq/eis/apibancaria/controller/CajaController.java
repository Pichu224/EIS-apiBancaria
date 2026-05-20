package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.service.interfaces.CajaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cajas")
public class CajaController {

    private final CajaService cajaService;

    public CajaController(CajaService cajaService) {
        this.cajaService = cajaService;
    }

    @PostMapping
    public ResponseEntity<Caja> crear(@RequestBody Caja caja) {
        cajaService.crear(caja);
        return ResponseEntity.status(HttpStatus.CREATED).body(caja);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Caja> recuperar(@PathVariable Long id) {
        return ResponseEntity.ok(cajaService.recuperar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Caja> actualizar(@PathVariable Long id, @RequestBody Caja caja) {
        caja.setIdCaja(id);
        cajaService.actualizar(caja);
        return ResponseEntity.ok(caja);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cajaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
