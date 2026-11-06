/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbercita.repository;

/**
 *
 * @author MSI
 */
import com.barbercita.domain.Pago;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    // Pago de una cita (una cita solo puede tener un pago)
    public Optional<Pago> findByCitaIdCita(Integer idCita);

    // Para revisar si la cita ya tiene un pago antes de registrar otro
    public boolean existsByCitaIdCita(Integer idCita);
}
