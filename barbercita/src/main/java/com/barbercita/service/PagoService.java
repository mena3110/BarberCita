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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// El barbero registra el pago de una cita que atendio (relacionado a HU-16)
@Slf4j
@Service
@RequiredArgsConstructor
public class PagoService {

    // Se utilizan los repositorios de cita y pago
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;

    // Metodos de pago que acepta la barberia
    private static final List<String> METODOS = List.of("Efectivo", "SINPE", "Tarjeta");

    public List<String> getMetodos() {
        return METODOS;
    }

    // Devuelve la cita solo si es del barbero y esta Pendiente y todavia no tiene pago
    @Transactional(readOnly = true)
    public Optional<Cita> getCitaParaPago(Integer idCita, String correoBarbero) {
        Optional<Cita> cita = citaRepository.findByIdCitaAndBarberoUsuarioCorreo(idCita, correoBarbero);
        if (cita.isPresent()
                && cita.get().getEstado().equals("Pendiente")
                && !pagoRepository.existsByCitaIdCita(idCita)) {
            return cita;
        }
        return Optional.empty();
    }

    // Guarda el pago y pasa la cita a Atendida en una misma transaccion:
    // si alguno de los dos pasos falla no se guarda ninguno
    @Transactional
    public void registrar(Integer idCita, String correoBarbero, Pago pago) {
        Optional<Cita> cita = getCitaParaPago(idCita, correoBarbero);
        if (cita.isEmpty()) {
            throw new IllegalArgumentException("La cita " + idCita + " no se puede pagar");
        }

        // El metodo tiene que ser uno de los permitidos
        if (!METODOS.contains(pago.getMetodo())) {
            throw new IllegalStateException("Metodo de pago no valido: " + pago.getMetodo());
        }

        // SINPE y Tarjeta necesitan el numero de comprobante, en efectivo no se guarda
        if (pago.getMetodo().equals("Efectivo")) {
            pago.setComprobante(null);
        } else if (pago.getComprobante() == null || pago.getComprobante().isBlank()) {
            throw new IllegalStateException("Falta el numero de comprobante");
        }

        pago.setCita(cita.get());
        pago.setDescripcion(cita.get().getServicio().getNombre());
        pago.setFecha(LocalDate.now());
        pagoRepository.save(pago);

        cita.get().setEstado("Atendida");
        citaRepository.save(cita.get());
        log.info("Se registro el pago de la cita " + idCita + " por " + pago.getMonto());
    }
}
