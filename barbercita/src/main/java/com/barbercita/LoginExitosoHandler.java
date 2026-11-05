package com.barbercita;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

//decide a donde va cada usuario despues de iniciar sesion, segun su rol real
@Component
public class LoginExitosoHandler implements AuthenticationSuccessHandler {

    //Pagina principal de cada rol
    public static final String RUTA_CLIENTE = "/cliente/reservar";
    //El barbero entra al inicio, donde ve su menu principal con la opcion "Mi agenda"
    public static final String RUTA_BARBERO = "/";
    //El admin tambien entra al inicio, donde ve su menu con la opcion "Administrar"
    public static final String RUTA_ADMIN = "/";

    //Aqui Spring guarda la pagina que el usuario intento abrir antes de iniciar sesion
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String destino = rutaSegunRol(authentication);

        //Si antes de iniciar sesion quiso entrar a una pagina de SU propia area, se le devuelve ahi
        //(ej: un cliente que toco "Mis citas"). Si era de otra area no se usa,
        //asi un barbero no termina en las paginas del cliente.
        SavedRequest guardada = requestCache.getRequest(request, response);
        if (guardada != null) {
            requestCache.removeRequest(request, response);
            String ruta = URI.create(guardada.getRedirectUrl()).getPath();
            ruta = ruta.substring(request.getContextPath().length());
            if (ruta.startsWith(areaSegunRol(authentication))) {
                destino = ruta;
            }
        }

        response.sendRedirect(request.getContextPath() + destino);
    }

    //Devuelve la pagina principal segun el rol del usuario que inicio sesion
    public static String rutaSegunRol(Authentication authentication) {
        if (tieneRol(authentication, "ROLE_ADMIN")) {
            return RUTA_ADMIN;
        }
        if (tieneRol(authentication, "ROLE_BARBERO")) {
            return RUTA_BARBERO;
        }
        if (tieneRol(authentication, "ROLE_CLIENTE")) {
            return RUTA_CLIENTE;
        }
        //Si no tiene ningun rol conocido no se manda al area del cliente, solo al inicio
        return "/";
    }

    //Carpeta de rutas que le pertenece a cada rol
    private static String areaSegunRol(Authentication authentication) {
        if (tieneRol(authentication, "ROLE_ADMIN")) {
            return "/admin/";
        }
        if (tieneRol(authentication, "ROLE_BARBERO")) {
            return "/barbero/";
        }
        if (tieneRol(authentication, "ROLE_CLIENTE")) {
            return "/cliente/";
        }
        return "/sin-area/";
    }

    //Revisa si el usuario tiene una autoridad, por ejemplo ROLE_BARBERO
    private static boolean tieneRol(Authentication authentication, String rol) {
        for (GrantedAuthority autoridad : authentication.getAuthorities()) {
            if (autoridad.getAuthority().equals(rol)) {
                return true;
            }
        }
        return false;
    }
}
