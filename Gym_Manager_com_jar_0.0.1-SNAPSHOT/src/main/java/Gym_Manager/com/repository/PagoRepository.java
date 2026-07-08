/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.repository;

import Gym_Manager.com.domain.Pago;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

    List<Pago> findByIdMiembroOrderByAnioDescMesDesc(Integer idMiembro);

    Optional<Pago> findByIdMiembroAndMesAndAnio(Integer idMiembro, Integer mes, Integer anio);
}
