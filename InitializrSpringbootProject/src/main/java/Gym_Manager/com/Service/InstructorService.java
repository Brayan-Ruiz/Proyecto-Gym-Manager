/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package Gym_Manager.com.service;

import Gym_Manager.com.domain.EstadoInstructor;
import Gym_Manager.com.domain.Instructor;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.domain.Usuario;
import Gym_Manager.com.repository.InstructorRepository;
import Gym_Manager.com.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final UsuarioRepository usuarioRepository;

    public InstructorService(InstructorRepository instructorRepository, UsuarioRepository usuarioRepository) {
        this.instructorRepository = instructorRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Instructor> getInstructores() {
        return instructorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Instructor> getInstructor(Integer idInstructor) {
        return instructorRepository.findById(idInstructor);
    }

    @Transactional
    public void save(Instructor instructor) {
        if (instructor.getIdInstructor() == null) {
            // Instructor nuevo: el ADMIN crea también su cuenta de acceso.
            Usuario usuario = new Usuario();
            usuario.setCorreo(instructor.getCorreo());
            usuario.setContrasena(instructor.getContrasena());
            usuario.setRol(RolUsuario.INSTRUCTOR);
            usuario.setActivo(true);
            usuarioRepository.save(usuario);

            instructor.setIdUsuario(usuario.getIdUsuario());
        } else {
            // Edición: se sincroniza el correo y el estado de acceso
            // con la cuenta de usuario existente, sin tocar la contraseña.
            if (instructor.getIdUsuario() != null) {
                usuarioRepository.findById(instructor.getIdUsuario()).ifPresent(usuario -> {
                    usuario.setCorreo(instructor.getCorreo());
                    usuario.setActivo(instructor.getEstado() == EstadoInstructor.ACTIVO);
                    usuarioRepository.save(usuario);
                });
            }
        }
        instructorRepository.save(instructor);
    }
    
    @Transactional(readOnly = true)
    public Optional<Instructor> getInstructorPorUsuario(Integer idUsuario) {
        return instructorRepository.findByIdUsuario(idUsuario);
    }

    @Transactional
    public void cambiarEstado(Integer idInstructor) {
        Instructor instructor = instructorRepository.findById(idInstructor)
                .orElseThrow(() -> new IllegalArgumentException("El instructor con ID " + idInstructor + " no existe."));

        EstadoInstructor nuevoEstado = instructor.getEstado() == EstadoInstructor.ACTIVO
                ? EstadoInstructor.INACTIVO
                : EstadoInstructor.ACTIVO;
        instructor.setEstado(nuevoEstado);

        if (instructor.getIdUsuario() != null) {
            usuarioRepository.findById(instructor.getIdUsuario()).ifPresent(usuario -> {
                usuario.setActivo(nuevoEstado == EstadoInstructor.ACTIVO);
                usuarioRepository.save(usuario);
            });
        }

        instructorRepository.save(instructor);
    }
}