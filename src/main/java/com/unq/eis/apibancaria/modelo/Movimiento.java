package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.MontoInvalidoException;
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

    public Movimiento(@NonNull Long nroTransferencia, @NonNull Caja cajaUtilizada, @NonNull BigDecimal monto, @NonNull String descripcion) {
        this.nroTransferencia = nroTransferencia;
        this.cajaUtilizada = cajaUtilizada;
        this.validarMonto(monto);
        this.monto = monto;
        // Esto puede ser modificado para que quede mejor la descricion.
        this.descripcion = "NroCaja : " + cajaUtilizada.getNroCaja() + " realizo transferencia hacia la caja : " + descripcion;
    }

    private void validarMonto(BigDecimal monto) {
        if(monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInvalidoException("El monto debe ser mayor a cero!");
        }
    }
}