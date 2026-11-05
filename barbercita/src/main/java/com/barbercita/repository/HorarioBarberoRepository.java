package com.barbercita.repository;

import com.barbercita.domain.HorarioBarbero;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioBarberoRepository extends JpaRepository<HorarioBarbero, Integer> {

    //Horarios de un barbero en un dia de la semana (1 = lunes ... 7 = domingo)
    public List<HorarioBarbero> findByBarberoIdBarberoAndDiaSemana(Integer idBarbero, Integer diaSemana);
}