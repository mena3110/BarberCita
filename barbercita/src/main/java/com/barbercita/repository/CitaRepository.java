package com.barbercita.repository;

import com.barbercita.domain.Cita;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    //Citas de un barbero en una fecha que no esten en el estado indicado (por ejemplo, Cancelada)
    public List<Cita> findByBarberoIdBarberoAndFechaAndEstadoNot(Integer idBarbero, LocalDate fecha, String estado);

    // Para encontrar citas de un cliente en un estado desde una fecha, ordenadas por fecha y hora (HU-08)
    public List<Cita> findByClienteCorreoAndEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraAsc(String correo, String estado, LocalDate fecha);

    // Para encontrar todas las citas de un cliente, de la mas reciente a la mas antigua
    public List<Cita> findByClienteCorreoOrderByFechaDescHoraDesc(String correo);

    // Para buscar la cita solo si es del cliente para que nadie vea ni cancele citas de otros (HU-09)
    public Optional<Cita> findByIdCitaAndClienteCorreo(Integer idCita, String correo);

    // Para buscar la cita solo si es del barbero que inicio sesion (HU-16)
    public Optional<Cita> findByIdCitaAndBarberoUsuarioCorreo(Integer idCita, String correo);
    
}
