/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.service;

import Gym_Manager.com.domain.CategoriaInventario;
import Gym_Manager.com.domain.Inventario;
import Gym_Manager.com.repository.InventarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Inventario> getInventario() {
        return inventarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Inventario> getInventarioPorCategoria(CategoriaInventario categoria) {
        return inventarioRepository.findByCategoria(categoria);
    }

    @Transactional(readOnly = true)
    public Optional<Inventario> getArticulo(Integer idInventario) {
        return inventarioRepository.findById(idInventario);
    }

    @Transactional
    public void save(Inventario inventario) {
        inventarioRepository.save(inventario);
    }

    @Transactional
    public void delete(Integer idInventario) {
        if (!inventarioRepository.existsById(idInventario)) {
            throw new IllegalArgumentException("El artículo con ID " + idInventario + " no existe.");
        }
        try {
            inventarioRepository.deleteById(idInventario);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("No se puede eliminar el artículo. Tiene datos asociados.", e);
        }
    }
}
