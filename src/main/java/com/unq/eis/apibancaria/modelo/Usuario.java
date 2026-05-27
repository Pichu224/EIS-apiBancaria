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
            fetch = FetchType.LAZY,  // Trae solo las cajas si es necesario, es decir, cuando se las llama.
            orphanRemoval = true)   // Permite que si se borra una caja, cuando le pegemos a la BD, este se actualiza sola al pasarle el usuario. Evitando tener que borrar la caja con otra peticion a la BD.
    private final List<Caja> cajas = new ArrayList<>();

    public Usuario(@NonNull String email, @NonNull String contrasenia, @NonNull String nombre, @NonNull String apellido, @NonNull String dni) {
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

    public Usuario(@NonNull String email, @NonNull String contrasenia) {
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
    }
    public Usuario(Long id, String email, String contrasenia){
        this.idUsuario = id;
        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
    }

    private String validarMail(@NonNull String email){
        if (!email.contains("@"))
            throw new MailInvalidoException("El mail es vacío o es inválido!");
        return email;
    }

    private String validarContrasenia(@NonNull String contrasenia){
        if (contrasenia.length() < 4)
            throw new ContraseniaVaciaException("La contraseña no puede ser vacia!");
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

    public BigDecimal consultarSaldo(@NonNull Caja caja) {
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
