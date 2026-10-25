package com.barbercita.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "servicio")
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer idServicio;

    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50)
    private String nombre;

    @Column(length = 255)
    @Size(max = 255)
    private String descripcion;

    @Column(precision = 12, scale = 2)
    @NotNull
    @DecimalMin(value = "0.00", inclusive = true)
    private BigDecimal precio;

    //duracion del servicio en minutos
    @NotNull
    @Min(value = 1)
    private Integer duracionMin;

    @Column(length = 1024)
    @Size(max = 1024)
    private String imagenUrl;

    @Column(nullable = false)
    private boolean activo;
}