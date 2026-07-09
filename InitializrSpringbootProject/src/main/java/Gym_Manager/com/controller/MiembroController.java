/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.Miembro;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.service.MiembroService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Controller
@RequestMapping("/miembro")
public class MiembroController {

    private final MiembroService miembroService;

    public MiembroController(MiembroService miembroService) {
        this.miembroService = miembroService;
    }

    @GetMapping("/listado")
    public String listado(Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        var miembros = miembroService.getMiembros();
        model.addAttribute("miembros", miembros);
        model.addAttribute("totalMiembros", miembros.size());
        return "miembro/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        model.addAttribute("miembro", new Miembro());
        return "miembro/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Miembro miembro, HttpSession session, RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        miembroService.save(miembro);
        redirectAttributes.addFlashAttribute("todoOk", "Miembro guardado correctamente.");
        return "redirect:/miembro/listado";
    }

    @PostMapping("/cambiar-estado")
    public String cambiarEstado(@org.springframework.web.bind.annotation.RequestParam Integer idMiembro,
                                 HttpSession session, RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        try {
            miembroService.cambiarEstado(idMiembro);
            redirectAttributes.addFlashAttribute("todoOk", "Estado del miembro actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El miembro no existe.");
        }
        return "redirect:/miembro/listado";
    }

    @GetMapping("/modificar/{idMiembro}")
    public String modificar(@PathVariable Integer idMiembro, Model model, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        Optional<Miembro> miembroOpt = miembroService.getMiembro(idMiembro);
        if (miembroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El miembro no existe.");
            return "redirect:/miembro/listado";
        }
        model.addAttribute("miembro", miembroOpt.get());
        return "miembro/formulario";
    }

    /**
     * Solo el rol ADMIN puede gestionar miembros.
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
