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
import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

// Para el registro de los avisos que se le envian al usuario sobre sus citas
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "notificacion")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Integer idNotificacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    // la cita es opcional, puede ser un aviso general
    @ManyToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @Column(nullable = false, length = 255)
    @NotBlank
    @Size(max = 255)
    private String descripcion;

    // enviada o fallida
    @Column(nullable = false, length = 20)
    @Size(max = 20)
    private String estado;

    private LocalDateTime fechaEnvio;
}
