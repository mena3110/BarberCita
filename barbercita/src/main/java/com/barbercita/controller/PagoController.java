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
import com.barbercita.domain.Pago;
import com.barbercita.service.PagoService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// codigo para que el barbero registre el pago de una cita desde su agenda - HU-16
@Controller
@RequiredArgsConstructor
@RequestMapping("/barbero/pago")
public class PagoController {

    //se debe comunicar con la capa de servicio
    private final PagoService pagoService;
    private final MessageSource messageSource;

    // Formulario para registrar el pago de una cita
    @GetMapping("/registrar/{idCita}")
    public String registrar(@PathVariable Integer idCita,
            Principal principal,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Cita> cita = pagoService.getCitaParaPago(idCita, principal.getName());
        if (cita.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("pago.error01", null, Locale.getDefault()));
            return "redirect:/barbero/agenda";
        }

        //E l monto se propone con el precio del servicio de la cita
        Pago pago = new Pago();
        pago.setMonto(cita.get().getServicio().getPrecio());
        pago.setMetodo("Efectivo");

        model.addAttribute("cita", cita.get());
        model.addAttribute("pago", pago);
        model.addAttribute("metodos", pagoService.getMetodos());
        return "barbero/pago/registrar";
    }

    // Ruta para guardar el pago
    @PostMapping("/guardar")
    public String guardar(@RequestParam Integer idCita,
            @Valid Pago pago,
            BindingResult bindingResult,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        // Si el monto o el metodo vienen mal se vuelve al formulario
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("pago.error02", null, Locale.getDefault()));
            return "redirect:/barbero/pago/registrar/" + idCita;
        }

        String titulo = "todoOk";
        String mensaje = "pago.ok";
        try {
            pagoService.registrar(idCita, principal.getName(), pago);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            mensaje = "pago.error01";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("pago.error02", null, Locale.getDefault()));
            return "redirect:/barbero/pago/registrar/" + idCita;
        }

        redirectAttributes.addFlashAttribute(titulo, messageSource.getMessage(mensaje, null, Locale.getDefault()));
        return "redirect:/barbero/agenda";
    }
}
