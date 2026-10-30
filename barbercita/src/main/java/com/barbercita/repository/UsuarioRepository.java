package com.barbercita.repository;

import com.barbercita.domain.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    //Se busca el usuario por su correo (el correo es el que se usa para iniciar sesion)
    public Optional<Usuario> findByCorreo(String correo);
}