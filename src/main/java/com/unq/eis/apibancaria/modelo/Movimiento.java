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

    public Movimiento(Long nroTransferencia, Caja cajaUtilizada,BigDecimal monto,String descripcion) {
        this.nroTransferencia = nroTransferencia;
        this.cajaUtilizada = cajaUtilizada;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    //No veo necesario hacer validaciones, ya que al ser llamado ya paso por todas validaciones de las demás clases.
    // Ojo con ésto (lo dije alann), pq que hayan pasado por otras validaciones no signfica que sea una instacia correcta
    // de la clase, es decir, cada clase debería de asegurar su propia integridad, no darle esa responsabilidad a las otras...
}