/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.domain;

import jakarta.persistence.*;
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
@Table(name = "inventario")
public class Inventario implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idInventario;

    @Column(name = "nombre_articulo", nullable = false, length = 100)
    @NotNull
    @Size(max = 100)
    private String nombreArticulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @NotNull
    private CategoriaInventario categoria;

    @Column(nullable = false)
    @NotNull
    private Integer cantidad = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_conservacion", nullable = false, length = 20)
    private EstadoConservacion estadoConservacion = EstadoConservacion.BUENO;

    @Column(length = 255)
    private String descripcion;
}