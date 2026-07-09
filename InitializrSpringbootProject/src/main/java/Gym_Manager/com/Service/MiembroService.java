/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.service;

import Gym_Manager.com.domain.EstadoMiembro;
import Gym_Manager.com.domain.Miembro;
import Gym_Manager.com.repository.MiembroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Service
public class MiembroService {

    private final MiembroRepository miembroRepository;

    public MiembroService(MiembroRepository miembroRepository) {
        this.miembroRepository = miembroRepository;
    }

    @Transactional(readOnly = true)
    public List<Miembro> getMiembros() {
        return miembroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Miembro> getMiembro(Integer idMiembro) {
        return miembroRepository.findById(idMiembro);
    }

    @Transactional
    public void save(Miembro miembro) {
        miembroRepository.save(miembro);
    }

    @Transactional
    public void cambiarEstado(Integer idMiembro) {
        Miembro miembro = miembroRepository.findById(idMiembro)
                .orElseThrow(() -> new IllegalArgumentException("El miembro con ID " + idMiembro + " no existe."));

        miembro.setEstado(miembro.getEstado() == EstadoMiembro.ACTIVO
                ? EstadoMiembro.BLOQUEADO
                : EstadoMiembro.ACTIVO);

        miembroRepository.save(miembro);
    }
}