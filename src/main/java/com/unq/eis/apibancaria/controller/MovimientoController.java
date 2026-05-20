package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.modelo.Movimiento;
import com.unq.eis.apibancaria.service.interfaces.MovimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<Movimiento> crear(@RequestBody Movimiento movimiento) {
        movimientoService.crear(movimiento);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> recuperar(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.recuperar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> actualizar(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        movimiento.setIdMovimiento(id);
        movimientoService.actualizar(movimiento);
        return ResponseEntity.ok(movimiento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        movimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
