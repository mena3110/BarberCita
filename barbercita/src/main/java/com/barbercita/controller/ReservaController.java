package com.barbercita.controller;

import com.barbercita.domain.Barbero;
import com.barbercita.domain.Servicio;
import com.barbercita.service.ReservaService;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//HU-07: el cliente escoge servicio, barbero, fecha y hora para reservar su cita
@Controller
@RequiredArgsConstructor
@RequestMapping("/cliente/reservar")
public class ReservaController {

    //se debe comunicar con la capa de servicio
    private final ReservaService reservaService;
    private final MessageSource messageSource;

    @GetMapping
    public String reservar(@RequestParam(required = false) Integer servicio,
            @RequestParam(required = false) Integer barbero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            Model model) {

        Servicio servicioElegido = null;
        if (servicio != null) {
            servicioElegido = reservaService.getServicio(servicio).orElse(null);
        }

        //solo se muestran los barberos que hacen el servicio escogido
        var barberos = reservaService.getBarberos(servicio);

        //si el barbero escogido no hace ese servicio, se quita la seleccion
        Barbero barberoElegido = null;
        if (barbero != null) {
            for (Barbero b : barberos) {
                if (b.getIdBarbero().equals(barbero)) {
                    barberoElegido = b;
                }
            }
        }

        //los proximos 6 dias para la agenda
        List<LocalDate> dias = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            dias.add(LocalDate.now().plusDays(i));
        }

        //las horas solo se calculan cuando ya escogio servicio, barbero y fecha
        List<LocalTime> horas = new ArrayList<>();
        if (servicioElegido != null && barberoElegido != null && fecha != null) {
            horas = reservaService.getHorasDisponibles(barberoElegido, servicioElegido, fecha);
        }
        //si la hora escogida ya no esta libre, se quita la seleccion
        if (!horas.contains(hora)) {
            hora = null;
        }

        model.addAttribute("servicios", reservaService.getServicios());
        model.addAttribute("barberos", barberos);
        model.addAttribute("dias", dias);
        model.addAttribute("horas", horas);
        model.addAttribute("servicioElegido", servicioElegido);
        model.addAttribute("barberoElegido", barberoElegido);
        model.addAttribute("fechaElegida", fecha);
        model.addAttribute("horaElegida", hora);
        return "reserva/reservar";
    }

    //Ruta para confirmar y guardar la cita
    @PostMapping("/confirmar")
    public String confirmar(@RequestParam Integer servicio,
            @RequestParam Integer barbero,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        //TEMPORAL: mientras Persona 1 agrega el login se usa el cliente de prueba
        String correo = "cliente@barbercita.com";
        if (principal != null) {
            correo = principal.getName();
        }

        String titulo = "todoOk";
        String mensaje = "reserva.ok";
        try {
            reservaService.reservar(correo, servicio, barbero, fecha, hora);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "reserva.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = "reserva.error02";
        }

        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));

        //si hubo error se vuelve a la reserva con lo que ya habia escogido
        if (titulo.equals("error")) {
            redirectAttributes.addAttribute("servicio", servicio);
            redirectAttributes.addAttribute("barbero", barbero);
            redirectAttributes.addAttribute("fecha", fecha);
        }
        //cuando exista la pagina de Mis citas se puede redirigir alla
        return "redirect:/cliente/reservar";
    }
}