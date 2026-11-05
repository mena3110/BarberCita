
package com.barbercita.controller;

import com.barbercita.service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/servicios")
public class ServicioClienteController {
    
       private final ServicioService servicioService;
       
       @GetMapping("")
    public String listado(Model model) {

        model.addAttribute(
                "servicios",
                servicioService.getServicios(true));
        return "cliente/servicio/listado";
    }
}
