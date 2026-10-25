package com.barbercita.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "horario_barbero")
public class HorarioBarbero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @ManyToOne
    @JoinColumn(name = "id_barbero")
    private Barbero barbero;

    //1 = lunes, 2 = martes ... 7 = domingo
    @NotNull
    @Min(value = 1)
    @Max(value = 7)
    private Integer diaSemana;

    @NotNull
    private LocalTime horaInicio;

    @NotNull
    private LocalTime horaFin;
}