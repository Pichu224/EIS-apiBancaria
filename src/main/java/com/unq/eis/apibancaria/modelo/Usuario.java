package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "dni")
        }
)
@ToString
@NoArgsConstructor // Necesita esta etiqueta, ya que no permite que no haya una clase sin constructor por defecto.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter()
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @NonNull
    private String contrasenia;

    @Column(length = 100)
    @Setter
    private String nombre;

    @Column(length = 100)
    @Setter
    private String apellido;

    @Column(unique = true, length = 20)
    @Setter
    private String dni;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private final List<Caja> cajas = new ArrayList<>();

    public Usuario(String email, String contrasenia, String nombre, String apellido, String dni) {
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public Usuario(String email, String contrasenia) {
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
    }

    public Usuario(Long id, String email,  String contrasenia) {
        this.idUsuario = id;
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
    }

    private String validarMail( String email){
        if (email == null || !email.contains("@"))
            throw new MailInvalidoException("El mail es vacío o es inválido!");
        return email;
    }

    private String validarContrasenia( String contrasenia){
        if (contrasenia == null || contrasenia.length() < 3)
            throw new ContraseniaVaciaException("La contraseña tiene que tener al menos 4 carácteres!");
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = this.validarContrasenia(contrasenia);
    }

    public void setEmail(String email) {
        this.email = this.validarMail(email);
    }

    public void addCaja(Caja caja){
        this.cajas.add(Objects.requireNonNull(caja, "La caja no puede ser null"));
    }

    public void removeCaja(Caja caja){
        if(this.laCajaMePertenece(caja))
            this.cajas.remove(caja);
    }

    public BigDecimal consultarSaldo( Caja caja) {
        if (!this.laCajaMePertenece(caja))
            throw new CajaInexistenteException("No exite la caja");
        return caja.getSaldo();
    }

    public void ingresarDinero(BigDecimal monto, Caja caja){
        if (!this.laCajaMePertenece(caja))
            throw new CajaInexistenteException("La caja no pertenece al Usuario");
        caja.depositar(monto);
    }

    private boolean laCajaMePertenece(Caja caja){
        return this.getCajas().contains(caja);
    }
}
