package com.barbercita.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cita")
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    //el cliente es un usuario con rol Cliente
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "id_barbero")
    private Barbero barbero;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private LocalTime hora;

    //Pendiente, Atendida o Cancelada
    @Column(nullable = false, length = 20)
    @Size(max = 20)
    private String estado;

    @Column(length = 255)
    @Size(max = 255)
    private String observaciones;
}