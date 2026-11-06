/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.domain;

/**
 *
 * @author MSI
 */

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

//Para enlaces temporales para recuperar la contrasena, a continuar desarrollando en el avance 3.
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "token_recuperacion")
public class TokenRecuperacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Integer idToken;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(unique = true, nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String token;

    // Activo o Usado o vencido
    @Column(nullable = false, length = 20)
    @Size(max = 20)
    private String estado;

    //fecha y hora en que vence el enlace
    private LocalDateTime fecha;
}
