package Gym_Manager.com.controller;

import Gym_Manager.com.domain.CategoriaInventario;
import Gym_Manager.com.domain.Inventario;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.service.InventarioService;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping("/listado")
    public String listado(@RequestParam(required = false) CategoriaInventario categoria,
                           Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }

        List<Inventario> articulos = (categoria == null)
                ? inventarioService.getInventario()
                : inventarioService.getInventarioPorCategoria(categoria);

        model.addAttribute("articulos", articulos);
        model.addAttribute("totalArticulos", articulos.size());
        model.addAttribute("categoriaSeleccionada", categoria);
        return "inventario/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        model.addAttribute("articulo", new Inventario());
        return "inventario/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Inventario articulo, HttpSession session, RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        inventarioService.save(articulo);
        redirectAttributes.addFlashAttribute("todoOk", "Artículo guardado correctamente.");
        return "redirect:/inventario/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idInventario, HttpSession session,
                            RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        String titulo = "todoOk";
        String detalle = "Artículo eliminado correctamente.";
        try {
            inventarioService.delete(idInventario);
        } catch (IllegalArgumentException e) {
            titulo = "error";
            detalle = "El artículo no existe.";
        } catch (IllegalStateException e) {
            titulo = "error";
            detalle = "No se puede eliminar, tiene datos asociados.";
        }
        redirectAttributes.addFlashAttribute(titulo, detalle);
        return "redirect:/inventario/listado";
    }

    @GetMapping("/modificar/{idInventario}")
    public String modificar(@PathVariable Integer idInventario, Model model, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        Optional<Inventario> articuloOpt = inventarioService.getArticulo(idInventario);
        if (articuloOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El artículo no existe.");
            return "redirect:/inventario/listado";
        }
        model.addAttribute("articulo", articuloOpt.get());
        return "inventario/formulario";
    }

    /**
     * Solo el rol ADMIN puede gestionar el inventario.
     */
    private String validarAcceso(HttpSession session) {
        Object rolActivo = session.getAttribute("rolActivo");

        if (rolActivo == null) {
            return "redirect:/login";
        }
        if (!rolActivo.equals(RolUsuario.ADMIN.name())) {
            return "redirect:/acceso-denegado";
        }
        return null;
    }
}