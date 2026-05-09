package com.unq.eis.apibancaria.modelo;

import com.unq.eis.apibancaria.exception.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor // Necesita esta etiqueta ya que no permite que no haya una clase sin constructor por defecto.
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String contraseña;

    @Column(length = 100)
    private String nombre;
    private String apellido;

    @Column(length = 20)
    private String dni;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    public Usuario(String email, String contraseña, String nombre, String apellido, String dni) {

        this.email = this.validarMail(email);
        this.contraseña = this.validarContraseña(contraseña);
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
    private String validarContraseña(String contraseña){
        if (contraseña == null || contraseña.isEmpty()){
            throw new ContraseñaVaciaException("La contraseña no puede ser vacia!");
        }
        return contraseña;
    }



}
