package com.unq.eis.apibancaria.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "cajaUtilizada")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRealizado = LocalDateTime.now();

    private String descripcion;

    @Column(nullable = false)
    private Long nroTransferencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_caja", nullable = false)
    private Caja cajaUtilizada;

    public Movimiento(Long nroTransferencia, Caja cajaUtilizada,String descripcion) {
        this.nroTransferencia = nroTransferencia;
        this.cajaUtilizada = cajaUtilizada;
        this.descripcion = descripcion;
        // El resto de valores se deberan setear una vez hecha la transferencia,
        // utilizando los setters de la clase para luego persistir como corresponde.
    }
}