/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.repository;

/**
 *
 * @author Brayan Ruiz Valverde
 */

import Gym_Manager.com.domain.Rutina;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutinaRepository extends JpaRepository<Rutina, Integer> {

    List<Rutina> findByIdInstructorOrderByFechaCreacionDesc(Integer idInstructor);

    List<Rutina> findByIdMiembroOrderByFechaCreacionDesc(Integer idMiembro);
}
