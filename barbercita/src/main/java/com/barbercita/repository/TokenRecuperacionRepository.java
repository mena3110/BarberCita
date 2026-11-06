/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.repository;

/**
 *
 * @author MSI
 */

import com.barbercita.domain.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRecuperacionRepository extends JpaRepository<TokenRecuperacion, Integer> {

    // Se busca el token que debe estar en el enlace de recuperacion
    public Optional<TokenRecuperacion> findByToken(String token);
}
