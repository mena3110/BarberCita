package com.barbercita.service;

import com.barbercita.domain.Usuario;
import com.barbercita.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Persona 1: Spring Security usa este servicio para buscar al usuario que quiere iniciar sesion
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final HttpSession session;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        //Buscar el usuario por su correo
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario " + correo));

        //Si no tiene contrasena o rol todavia no puede iniciar sesion
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()
                || usuario.getRol() == null || usuario.getRol().isBlank()) {
            throw new UsernameNotFoundException("El usuario " + correo + " no tiene contrasena o rol");
        }

        //Guardar el nombre y la foto en la sesion para mostrarlos en el menu
        session.setAttribute("usuarioNombre", usuario.getNombre());
        session.setAttribute("usuarioFoto", usuario.getFoto());

        //Se arma el usuario de Spring con su rol y si esta activo o no
        return User.builder()
                .username(usuario.getCorreo())
                .password(usuario.getPassword())
                .roles(usuario.getRol().toUpperCase())
                .disabled(!usuario.isActivo())
                .build();
    }
}
