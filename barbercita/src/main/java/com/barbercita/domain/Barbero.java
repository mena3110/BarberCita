package com.barbercita.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "barbero")
public class Barbero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_barbero")
    private Integer idBarbero;

    //El barbero es un usuario con informacion adicional (el nombre esta en usuario)
    @OneToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(length = 100)
    @Size(max = 100)
    private String especialidad;

    @Column(nullable = false)
    private boolean activo;

    //Servicios que puede hacer el barbero (tabla barbero_servicio)
    @ManyToMany
    @JoinTable(name = "barbero_servicio",
            joinColumns = @JoinColumn(name = "id_barbero"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio"))
    private List<Servicio> servicios = new ArrayList<>();
}