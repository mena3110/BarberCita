package com.barbercita.controller;

import com.barbercita.LoginExitosoHandler;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//pantallas del login y de acceso denegado
@Controller
public class LoginController {

    //Mostrar la pantalla de login
    @GetMapping("/login")
    public String login(Authentication authentication) {
        //Si ya inicio sesion no tiene que volver al login, se envia al area de su rol
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:" + LoginExitosoHandler.rutaSegunRol(authentication);
        }
        return "login";
    }

    //Cuando el usuario no tiene el rol para entrar a una pagina
    @GetMapping("/acceso_denegado")
    public String accesoDenegado() {
        return "acceso_denegado";
    }
}
