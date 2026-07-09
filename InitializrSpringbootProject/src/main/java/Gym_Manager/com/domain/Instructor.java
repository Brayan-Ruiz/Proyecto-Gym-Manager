/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Data
@Entity
@Table(name = "instructor")
public class Instructor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instructor")
    private Integer idInstructor;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, length = 60)
    @NotNull
    @Size(max = 60)
    private String nombre;

    @Column(name = "primer_apellido", nullable = false, length = 60)
    @NotNull
    @Size(max = 60)
    private String primerApellido;

    @Column(name = "segundo_apellido", length = 60)
    private String segundoApellido;

    private Integer edad;

    @Column(nullable = false, unique = true, length = 100)
    @NotNull
    @Email
    private String correo;

    @Column(nullable = false, unique = true, length = 20)
    @NotNull
    private String cedula;

    @Column(name = "numero_contrato", nullable = false, unique = true, length = 20)
    @NotNull
    private String numeroContrato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInstructor estado = EstadoInstructor.ACTIVO;

    // Campo transitorio: solo se encuentra al crear un instructor.
    @Transient
    private String contrasena;
}