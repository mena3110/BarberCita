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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

// Clase realizada para el pago de una cita atendida (HU-16)
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    // una cita solo puede tener un pago
    @OneToOne
    @JoinColumn(name = "id_cita")
    private Cita cita;

    @Column(precision = 12, scale = 2)
    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal monto;

    // En efectivo o SINPE o Tarjeta
    @Column(nullable = false, length = 20)
    @NotBlank
    @Size(max = 20)
    private String metodo;

    // numero de comprobante, solo para SINPE o Tarjeta
    @Column(length = 50)
    @Size(max = 50)
    private String comprobante;

    @Column(length = 255)
    @Size(max = 255)
    private String descripcion;

    //la fecha la pone el sistema al registrar el pago
    private LocalDate fecha;
}
