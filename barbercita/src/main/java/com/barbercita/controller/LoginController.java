package com.barbercita.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Persona 1: pantallas del login y de acceso denegado
@Controller
public class LoginController {

    //Mostrar la pantalla de login
    @GetMapping("/login")
    public String login(Principal principal) {
        //Si ya inicio sesion no tiene que volver al login
        if (principal != null) {
            return "redirect:/";
        }
        return "login";
    }

    //Cuando el usuario no tiene el rol para entrar a una pagina
    @GetMapping("/acceso_denegado")
    public String accesoDenegado() {
        return "acceso_denegado";
    }
}
