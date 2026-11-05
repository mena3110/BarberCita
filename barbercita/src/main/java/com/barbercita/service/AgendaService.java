package com.barbercita.service;

import com.barbercita.domain.Barbero;
import com.barbercita.domain.Cita;
import com.barbercita.repository.BarberoRepository;
import com.barbercita.repository.CitaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final BarberoRepository barberoRepository;
    private final CitaRepository citaRepository;

    @Transactional(readOnly = true)
    public List<Cita> getAgendaDia(
            String correo,
            LocalDate fecha) {

        Barbero barbero = barberoRepository
                .findByUsuarioCorreo(correo)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                                "Barbero no encontrado"));

        List<Cita> citas
                = citaRepository
                        .findByBarberoIdBarberoAndFechaAndEstadoNot(
                                barbero.getIdBarbero(),
                                fecha,
                                "Cancelada");

        citas.sort(Comparator.comparing(Cita::getHora));

        return citas;
    }

    @Transactional(readOnly = true)
    public Optional<Cita> getProximaCita(String correo) {

        LocalTime ahora = LocalTime.now();

        List<Cita> citas
                = getAgendaDia(
                        correo,
                        LocalDate.now());

        return citas.stream()
                .filter(c -> c.getHora().isAfter(ahora))
                .min(Comparator.comparing(Cita::getHora));
    }

 
@Transactional
public void actualizarEstado(
        Integer idCita,
        String estado) {

    Cita cita = citaRepository
            .findById(idCita)
            .orElseThrow(() ->
                    new IllegalArgumentException("Cita no encontrada"));

    cita.setEstado(estado);

    citaRepository.save(cita);
}
}
