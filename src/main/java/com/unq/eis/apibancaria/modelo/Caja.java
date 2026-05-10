package com.unq.eis.apibancaria.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
    private Long nroCaja;

    @Column(nullable = false, unique = true)
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCaja tipoCaja;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    public Caja(Long nroCaja, String alias, Usuario usuario){

        this.nroCaja = nroCaja;
        this.alias = alias;
        this.tipoCaja = TipoCaja.CajaAhorro;
        this.saldo = BigDecimal.ZERO;
        this.usuario = usuario;

    }

    public void depositarEnCaja(BigDecimal monto){

    }
}
