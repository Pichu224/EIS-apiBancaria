package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "dni")
        }
)
@ToString
@NoArgsConstructor // Necesita esta etiqueta ya que no permite que no haya una clase sin constructor por defecto.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String contrasenia;

    @Column(length = 100)
    private String nombre;

    @Column(length = 100)
    private String apellido;

    @Column(unique = true, length = 20)
    private String dni;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "usuario",
            cascade = CascadeType.ALL, // Trae solo las cajas si es necesario, es decir, cuando se las llama.
            orphanRemoval = true)   // Permite que si se borra una caja, cuando le pegemos a la BD, este se actualiza sola al pasarle el usuario. Evitando tener que borrar la caja con otra peticion a la BD.
    private List<Caja> cajas = new ArrayList<>();

    public Usuario(String email, String contrasenia, String nombre, String apellido, String dni) {

        this.email = this.validarMail(email);
        this.contrasenia = this.validarContrasenia(contrasenia);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaRegistro = LocalDateTime.now();
    }

    private String validarMail(String email){
        if (email == null  || ! email.contains("@"))
            throw new MailInvalidoException("El mail es vacio o no este no es valido!");
        return email;
    }
    private String validarContrasenia(String contraseña){
        if (contraseña == null || contraseña.isEmpty()){
            throw new ContraseniaVaciaException("La contraseña no puede ser vacia!");
        }
        return contraseña;
    }



}
