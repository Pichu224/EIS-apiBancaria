package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransferencia;

    @Column(nullable = false)
    private LocalDateTime fechaRealizado;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_origen", nullable = false)
    private Caja cajaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caja_destino", nullable = false)
    private Caja cajaDestino;

    public Transferencia(BigDecimal montoTotal, Caja cajaOrigen, Caja cajaDestino) {

        if(montoTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInvalidoException("El monto debe ser mayor a cero."); //Modificar
        }

        if(cajaOrigen == null || cajaDestino == null) {
            throw new CajasNoIngresadasException("Las cajas no pueden ser nulas."); //Modificar
        }

        if(cajaOrigen.equals(cajaDestino)) { // Modificar ya que se deberia chequear por Id.
            throw new CajasIgualesException(
                    "La caja origen y destino no pueden ser iguales"
            );
        }

        this.montoTotal = montoTotal;
        this.cajaOrigen = cajaOrigen;
        this.cajaDestino = cajaDestino;
        this.fechaRealizado = LocalDateTime.now();
    }
}
