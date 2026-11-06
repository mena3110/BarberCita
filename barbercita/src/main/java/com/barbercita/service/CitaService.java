/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.service;

/**
 *
 * @author MSI
 */
import com.barbercita.domain.Cita;
import com.barbercita.domain.Pago;
import com.barbercita.repository.CitaRepository;
import com.barbercita.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Codigo para citas del cliente (HU-08) y cancelar una cita (HU-09)
@Slf4j
@Service
@RequiredArgsConstructor
public class CitaService {

    // Utilizamos los repositorios de cita y pago
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;

    // El cliente puede cancelar hasta 2 horas antes de la cita
    private static final int HORAS_PARA_CANCELAR = 2;

    // Proximas citas del cliente: pendientes y que todavia no han pasado ordenadas por fecha y hora
    @Transactional(readOnly = true)
    public List<Cita> getProximasCitas(String correo) {
        var citas = citaRepository.findByClienteCorreoAndEstadoAndFechaGreaterThanEqualOrderByFechaAscHoraAsc(
                correo, "Pendiente", LocalDate.now());
        List<Cita> proximas = new ArrayList<>();
        for (Cita c : citas) {
            if (esProxima(c)) {
                proximas.add(c);
            }
        }
        return proximas;
    }

    // Historial - lee las citas del cliente *atendidas, canceladas o que ya pasaron*, de la mas reciente a la mas antigua
    @Transactional(readOnly = true)
    public List<Cita> getHistorial(String correo) {
        var citas = citaRepository.findByClienteCorreoOrderByFechaDescHoraDesc(correo);
        List<Cita> historial = new ArrayList<>();
        for (Cita c : citas) {
            if (!esProxima(c)) {
                historial.add(c);
            }
        }
        return historial;
    }

    // Para obtener los detalles de una cita solo si es del cliente que inicio sesion
    @Transactional(readOnly = true)
    public Optional<Cita> getCita(Integer idCita, String correo) {
        return citaRepository.findByIdCitaAndClienteCorreo(idCita, correo);
    }

    // Pago de la cita solo si ya se registro - por el barbero parte de la HU16
    @Transactional(readOnly = true)
    public Optional<Pago> getPago(Integer idCita) {
        return pagoRepository.findByCitaIdCita(idCita);
    }

    // Cambiamos la cita a cancelada para que el horario quede libre otra vez
    @Transactional
    public void cancelar(Integer idCita, String correo) {
        Optional<Cita> cita = citaRepository.findByIdCitaAndClienteCorreo(idCita, correo);

        // Solo se cancelan citas propias que esten pendientes
        if (cita.isEmpty() || !cita.get().getEstado().equals("Pendiente")) {
            throw new IllegalArgumentException("La cita " + idCita + " no existe o no esta pendiente");
        }

        // Tienen que faltar al menos 2 horas para el inicio de la cita
        LocalDateTime inicio = LocalDateTime.of(cita.get().getFecha(), cita.get().getHora());
        if (LocalDateTime.now().plusHours(HORAS_PARA_CANCELAR).isAfter(inicio)) {
            throw new IllegalStateException("Faltan menos de " + HORAS_PARA_CANCELAR + " horas para la cita " + idCita);
        }

        cita.get().setEstado("Cancelada");
        citaRepository.save(cita.get());
        log.info("El cliente " + correo + " cancelo la cita " + idCita);
    }

    // Una cita es proxima si esta pendiente y su fecha y hora todavia no han pasado
    private boolean esProxima(Cita cita) {
        LocalDateTime inicio = LocalDateTime.of(cita.getFecha(), cita.getHora());
        return cita.getEstado().equals("Pendiente") && inicio.isAfter(LocalDateTime.now());
    }
}

