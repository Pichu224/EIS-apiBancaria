package com.unq.eis.apibancaria.controller;

import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.service.interfaces.TransferenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    public TransferenciaController(TransferenciaService transferenciaService) {
        this.transferenciaService = transferenciaService;
    }

    @PostMapping
    public ResponseEntity<Transferencia> crear(@RequestBody Transferencia transferencia) {
        transferenciaService.crear(transferencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(transferencia);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transferencia> recuperar(@PathVariable Long id) {
        return ResponseEntity.ok(transferenciaService.recuperar(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transferencia> actualizar(@PathVariable Long id, @RequestBody Transferencia transferencia) {
        transferencia.setIdTransferencia(id);
        transferenciaService.actualizar(transferencia);
        return ResponseEntity.ok(transferencia);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        transferenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
