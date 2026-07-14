/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.Instructor;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.service.InstructorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @GetMapping("/listado")
    public String listado(Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        var instructores = instructorService.getInstructores();
        model.addAttribute("instructores", instructores);
        model.addAttribute("totalInstructores", instructores.size());
        return "instructor/listado";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        model.addAttribute("instructor", new Instructor());
        return "instructor/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Instructor instructor, HttpSession session, RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        instructorService.save(instructor);
        redirectAttributes.addFlashAttribute("todoOk", "Instructor guardado correctamente.");
        return "redirect:/instructor/listado";
    }

    @PostMapping("/cambiar-estado")
    public String cambiarEstado(@RequestParam Integer idInstructor, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        try {
            instructorService.cambiarEstado(idInstructor);
            redirectAttributes.addFlashAttribute("todoOk", "Estado del instructor actualizado.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "El instructor no existe.");
        }
        return "redirect:/instructor/listado";
    }

    @GetMapping("/modificar/{idInstructor}")
    public String modificar(@PathVariable Integer idInstructor, Model model, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        Optional<Instructor> instructorOpt = instructorService.getInstructor(idInstructor);
        if (instructorOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El instructor no existe.");
            return "redirect:/instructor/listado";
        }
        model.addAttribute("instructor", instructorOpt.get());
        return "instructor/formulario";
    }

    /**
     * ADMIN puede gestionar instructores.
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
