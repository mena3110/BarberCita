package com.barbercita.controller;

import com.barbercita.service.AgendaService;
import java.security.Principal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    String correo;

    /*
     * Versión definitiva cuando la autenticación esté lista:
     *
     * correo = principal.getName();
     *
     * El correo se utilizará para identificar al usuario
     * autenticado y obtener el barbero asociado.
     */
    if (principal != null) {
        correo = principal.getName();
    } else {
        correo = "carlos@barbercita.com"; // usuario temporal para pruebas
    }

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
}