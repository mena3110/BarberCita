/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.controller;

/**
 *
 * @author MSI
 */

import com.barbercita.domain.Cita;
import com.barbercita.service.CitaService;
import java.security.Principal;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Codigo para ver mis citas (HU-08) y cancelar una cita (HU-09)
@Controller
@RequiredArgsConstructor
@RequestMapping("/cliente/citas")
public class CitaController {

    // para comunicar con la capa de servicio
    private final CitaService citaService;
    private final MessageSource messageSource;

    // Pantalla de mis citas: proxima cita, proximas citas e historial
    @GetMapping
    public String listado(Principal principal, Model model) {
        //Obtener el correo del usuario que inicio sesion
        String correo = principal.getName();

        var citas = citaService.getProximasCitas(correo);
        model.addAttribute("citas", citas);
        //la proxima cita es la primera porque vienen ordenadas por fecha y hora
        model.addAttribute("proximaCita", citas.isEmpty() ? null : citas.get(0));
        model.addAttribute("historial", citaService.getHistorial(correo));
        return "cliente/cita/listado";
    }

    //Detalle de una cita del cliente
    @GetMapping("/detalle/{idCita}")
    public String detalle(@PathVariable Integer idCita,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Cita> cita = citaService.getCita(idCita, principal.getName());
        if (cita.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("cita.error01", null, Locale.getDefault()));
            return "redirect:/cliente/citas";
        }
        model.addAttribute("cita", cita.get());
        model.addAttribute("pago", citaService.getPago(idCita).orElse(null));
        return "cliente/cita/detalle";
    }

    // Ruta para cancelar la cita, se llama desde el modal de confirmacion como se vio en clase.
    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idCita,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "cita.cancelada";
        try {
            citaService.cancelar(idCita, principal.getName());
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "cita.error01";
        } catch (IllegalStateException e) {
            titulo = "error";
            mensaje = "cita.error02";
        }

        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/cliente/citas";
    }
}
