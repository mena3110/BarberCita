package com.barbercita.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String nombre;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String correo;

    @Column(length = 20)
    @Size(max = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean activo;

    //Persona 1 agrega aqui el rol, el password y la foto para el login
}