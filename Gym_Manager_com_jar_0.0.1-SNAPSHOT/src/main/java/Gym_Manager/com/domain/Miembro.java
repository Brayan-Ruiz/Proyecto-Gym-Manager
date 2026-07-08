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
@Table(name = "miembro")
public class Miembro implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miembro")
    private Integer idMiembro;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Sexo sexo;

    private Double peso;

    private Double estatura;

    @Column(nullable = false, unique = true, length = 100)
    @NotNull
    @Email
    private String correo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMiembro estado = EstadoMiembro.ACTIVO;
}