package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import com.unq.eis.apibancaria.exception.SaldoInsuficienteException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "usuario")
@Entity
@Table(
        name = "caja",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "nro_caja"),
                @UniqueConstraint(columnNames = "alias")
        }
)
public class Caja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCaja;

    @Column(nullable = false, unique = true)
    @NonNull
    private Long nroCaja;

    @Column(nullable = false, unique = true)
    @NonNull
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCaja tipoCaja;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @NonNull
    private Usuario usuario;

    public Caja(@NonNull Long nroCaja, @NonNull String alias, @NonNull Usuario usuario){
        this.nroCaja = nroCaja;
        this.alias = alias;
        this.tipoCaja = TipoCaja.CajaAhorro;
        this.usuario = usuario;
    }
  
    public void depositar(@NonNull BigDecimal monto) {
        this.validarMonto(monto);
        this.saldo = this.saldo.add(monto);
    }

    public void retirar(@NonNull BigDecimal monto) {
        this.validarMonto(monto);
        if(this.saldo.compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    private void validarMonto(BigDecimal monto) {
        if(monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MontoInvalidoException("El monto debe ser mayor a cero!");
        }
    }
}
