package com.barbercita.repository;

import com.barbercita.domain.Servicio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {

    //Consulta derivada para recuperar solo los servicios activos
    public List<Servicio> findByActivoTrue();
}