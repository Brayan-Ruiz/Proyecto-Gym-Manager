/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.service;

import Gym_Manager.com.domain.Rutina;
import Gym_Manager.com.repository.RutinaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Service
public class RutinaService {

    private final RutinaRepository rutinaRepository;

    public RutinaService(RutinaRepository rutinaRepository) {
        this.rutinaRepository = rutinaRepository;
    }

    @Transactional(readOnly = true)
    public List<Rutina> getRutinasPorInstructor(Integer idInstructor) {
        return rutinaRepository.findByIdInstructorOrderByFechaCreacionDesc(idInstructor);
    }

    @Transactional(readOnly = true)
    public List<Rutina> getRutinasPorMiembro(Integer idMiembro) {
        return rutinaRepository.findByIdMiembroOrderByFechaCreacionDesc(idMiembro);
    }

    @Transactional(readOnly = true)
    public Optional<Rutina> getRutina(Integer idRutina) {
        return rutinaRepository.findById(idRutina);
    }

    @Transactional
    public void save(Rutina rutina) {
        rutinaRepository.save(rutina);
    }
}