/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.repository;

/**
 *
 * @author MSI
 */

import com.barbercita.domain.Notificacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    // Para ordenar las notificaciones enviadas a un usuario de la mas reciente a la mas antigua / se continuara en el avance 3.
    public List<Notificacion> findByUsuarioCorreoOrderByFechaEnvioDesc(String correo);
}
