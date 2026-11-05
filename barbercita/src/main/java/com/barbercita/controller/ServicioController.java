package com.barbercita.controller;

import com.barbercita.domain.Servicio;
import com.barbercita.service.ServicioService;
import jakarta.validation.Valid;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/servicio")

public class ServicioController {

    private final ServicioService servicioService;
    private final MessageSource messageSource;

    @GetMapping("/listado")
    public String listado(Model model) {

        var servicios = servicioService.getServicios(false);

        model.addAttribute("servicios", servicios);
        model.addAttribute("totalServicios", servicios.size());

        return "admin/servicio/listado";
    }

// Ruta para crear o modificar servicios
    @PostMapping("/guardar")
    public String guardar(
            @Valid Servicio servicio,
            BindingResult bindingResult,
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "servicio.actualizado";

        if (bindingResult.hasErrors()) {

            titulo = "error";
            mensaje = "servicio.error04";

        } else {

            try {

                servicioService.save(servicio, imagenFile);

            } catch (DataIntegrityViolationException e) {

                titulo = "error";
                mensaje = "servicio.error05";
            }
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        mensaje,
                        null,
                        Locale.getDefault()));

        return "redirect:/admin/servicio/listado";
    }
// Ruta para desactivar servicios

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Integer idServicio,
            RedirectAttributes redirectAttributes) {

        String titulo = "todoOk";
        String mensaje = "servicio.eliminado";

        try {

            servicioService.delete(idServicio);

        } catch (IllegalArgumentException e) {

            titulo = "error";
            mensaje = "servicio.error01";

        } catch (Exception e) {

            titulo = "error";
            mensaje = "servicio.error03";
        }

        redirectAttributes.addFlashAttribute(
                titulo,
                messageSource.getMessage(
                        mensaje,
                        null,
                        Locale.getDefault()));

        return "redirect:/admin/servicio/listado";
    }

    @PostMapping("/activar")
    public String activar(
            @RequestParam Integer idServicio,
            RedirectAttributes redirectAttributes) {

        servicioService.activar(idServicio);

        return "redirect:/admin/servicio/listado";
    }
}
