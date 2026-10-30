package com.barbercita.repository;

import com.barbercita.domain.Barbero;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberoRepository extends JpaRepository<Barbero, Integer> {

    //Consulta derivada para recuperar solo los barberos activos
    public List<Barbero> findByActivoTrue();

    //Barberos activos que pueden hacer un servicio (tabla barbero_servicio)
    public List<Barbero> findByActivoTrueAndServiciosIdServicio(Integer idServicio);
}