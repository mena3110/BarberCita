package com.barbercita.controller;

import com.barbercita.service.AgendaService;
import java.security.Principal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/barbero/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    @GetMapping
    public String agenda(
            @RequestParam(required = false) String fecha,
            Principal principal,
            Model model) {

        LocalDate fechaConsulta;

        if (fecha != null && !fecha.isBlank()) {
            fechaConsulta = LocalDate.parse(fecha);
        } else {
            fechaConsulta = LocalDate.now();
        }

        String correo = principal.getName();

        model.addAttribute(
                "citas",
                agendaService.getAgendaDia(
                        correo,
                        fechaConsulta));

        model.addAttribute(
                "proximaCita",
                agendaService.getProximaCita(correo));

        model.addAttribute(
                "fechaSeleccionada",
                fechaConsulta);

        return "barbero/agenda/listado";
    }

    @PostMapping("/cita/actualizarEstado")
    public String actualizarEstado(
            @RequestParam Integer idCita,
            @RequestParam String estado) {

        agendaService.actualizarEstado(idCita, estado);

        return "redirect:/barbero/agenda";
    }
}
