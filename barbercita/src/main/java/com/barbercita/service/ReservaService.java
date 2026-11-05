package com.barbercita.service;

import com.barbercita.domain.Barbero;
import com.barbercita.domain.Cita;
import com.barbercita.domain.HorarioBarbero;
import com.barbercita.domain.Servicio;
import com.barbercita.domain.Usuario;
import com.barbercita.repository.BarberoRepository;
import com.barbercita.repository.CitaRepository;
import com.barbercita.repository.HorarioBarberoRepository;
import com.barbercita.repository.ServicioRepository;
import com.barbercita.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservaService {

    //Se utilizan los repositorios que ocupa la reserva
    private final ServicioRepository servicioRepository;
    private final BarberoRepository barberoRepository;
    private final HorarioBarberoRepository horarioBarberoRepository;
    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    //Cada cuantos minutos se ofrece un espacio en la agenda
    private static final int INTERVALO = 30;

    @Transactional(readOnly = true)
    public List<Servicio> getServicios() {
        return servicioRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> getServicio(Integer idServicio) {
        return servicioRepository.findById(idServicio);
    }

    //Si ya escogio un servicio, solo se muestran los barberos que lo hacen
    @Transactional(readOnly = true)
    public List<Barbero> getBarberos(Integer idServicio) {
        if (idServicio == null) {
            return barberoRepository.findByActivoTrue();
        } else {
            return barberoRepository.findByActivoTrueAndServiciosIdServicio(idServicio);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Barbero> getBarbero(Integer idBarbero) {
        return barberoRepository.findById(idBarbero);
    }

    //Calcula las horas libres de un barbero en una fecha, segun lo que dura el servicio
    @Transactional(readOnly = true)
    public List<LocalTime> getHorasDisponibles(Barbero barbero, Servicio servicio, LocalDate fecha) {
        List<LocalTime> horas = new ArrayList<>();
        int diaSemana = fecha.getDayOfWeek().getValue(); //1 = lunes ... 7 = domingo

        var horarios = horarioBarberoRepository.findByBarberoIdBarberoAndDiaSemana(barbero.getIdBarbero(), diaSemana);
        var citas = citaRepository.findByBarberoIdBarberoAndFechaAndEstadoNot(barbero.getIdBarbero(), fecha, "Cancelada");

        for (HorarioBarbero h : horarios) {
            LocalTime hora = h.getHoraInicio();
            //el servicio tiene que terminar antes de que termine el horario del barbero
            while (!hora.plusMinutes(servicio.getDuracionMin()).isAfter(h.getHoraFin())) {
                //no se muestran horas que ya pasaron ni horas que chocan con otra cita
                if (LocalDateTime.of(fecha, hora).isAfter(LocalDateTime.now())
                        && estaLibre(hora, servicio.getDuracionMin(), citas)) {
                    horas.add(hora);
                }
                hora = hora.plusMinutes(INTERVALO);
            }
        }
        return horas;
    }

    //Revisa que el espacio no choque con otra cita del barbero
    private boolean estaLibre(LocalTime inicio, int duracion, List<Cita> citas) {
        LocalTime fin = inicio.plusMinutes(duracion);
        for (Cita c : citas) {
            LocalTime inicioCita = c.getHora();
            LocalTime finCita = inicioCita.plusMinutes(c.getServicio().getDuracionMin());
            if (inicio.isBefore(finCita) && fin.isAfter(inicioCita)) {
                return false;
            }
        }
        return true;
    }

    //Guarda la cita del cliente con estado Pendiente
    @Transactional
    public void reservar(String correoCliente, Integer idServicio, Integer idBarbero, LocalDate fecha, LocalTime hora) {
        Optional<Usuario> cliente = usuarioRepository.findByCorreo(correoCliente);
        Optional<Servicio> servicio = servicioRepository.findById(idServicio);
        Optional<Barbero> barbero = barberoRepository.findById(idBarbero);

        //Se valida que existan los datos de la reserva
        if (cliente.isEmpty() || servicio.isEmpty() || barbero.isEmpty()) {
            throw new IllegalArgumentException("Faltan datos para la reserva");
        }

        //Se vuelve a revisar el horario por si otra persona lo reservo mientras tanto
        if (!getHorasDisponibles(barbero.get(), servicio.get(), fecha).contains(hora)) {
            throw new IllegalStateException("El horario ya no esta disponible: " + fecha + " " + hora);
        }

        Cita cita = new Cita();
        cita.setCliente(cliente.get());
        cita.setBarbero(barbero.get());
        cita.setServicio(servicio.get());
        cita.setFecha(fecha);
        cita.setHora(hora);
        cita.setEstado("Pendiente");
        citaRepository.save(cita);
        log.info("Se reservo la cita " + cita.getIdCita() + " para " + correoCliente);
    }
}