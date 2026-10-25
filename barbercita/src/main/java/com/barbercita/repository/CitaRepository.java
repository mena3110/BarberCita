package com.barbercita.repository;

import com.barbercita.domain.Cita;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    //Citas de un barbero en una fecha que no esten en el estado indicado (por ejemplo, Cancelada)
    public List<Cita> findByBarberoIdBarberoAndFechaAndEstadoNot(Integer idBarbero, LocalDate fecha, String estado);
}