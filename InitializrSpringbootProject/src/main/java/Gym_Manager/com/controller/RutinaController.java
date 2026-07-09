/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.Instructor;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.domain.Rutina;
import Gym_Manager.com.domain.Usuario;
import Gym_Manager.com.service.InstructorService;
import Gym_Manager.com.service.MiembroService;
import Gym_Manager.com.service.RutinaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
@RequestMapping("/rutina")
public class RutinaController {

    private final RutinaService rutinaService;
    private final InstructorService instructorService;
    private final MiembroService miembroService;

    public RutinaController(RutinaService rutinaService, InstructorService instructorService,
                             MiembroService miembroService) {
        this.rutinaService = rutinaService;
        this.instructorService = instructorService;
        this.miembroService = miembroService;
    }

    @GetMapping("/listado")
    public String listado(Model model, HttpSession session) {
        Object redireccion = validarAccesoInstructor(session, model);
        if (redireccion != null) {
            return (String) redireccion;
        }

        Instructor instructor = (Instructor) model.getAttribute("instructorSesion");
        var rutinas = rutinaService.getRutinasPorInstructor(instructor.getIdInstructor());
        model.addAttribute("rutinas", rutinas);
        model.addAttribute("totalRutinas", rutinas.size());
        return "rutina/listado";
    }

    @GetMapping("/nueva")
    public String nueva(Model model, HttpSession session) {
        Object redireccion = validarAccesoInstructor(session, model);
        if (redireccion != null) {
            return (String) redireccion;
        }

        model.addAttribute("rutina", new Rutina());
        model.addAttribute("miembros", miembroService.getMiembros());
        return "rutina/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Rutina rutina, HttpSession session, Model model,
                           RedirectAttributes redirectAttributes) {
        Object redireccion = validarAccesoInstructor(session, model);
        if (redireccion != null) {
            return (String) redireccion;
        }

        // El instructor solo puede asignarse rutinas a sí mismo, nunca a
        // nombre de otro instructor, sin importar lo que venga del formulario.
        Instructor instructor = (Instructor) model.getAttribute("instructorSesion");
        rutina.setIdInstructor(instructor.getIdInstructor());

        rutinaService.save(rutina);
        redirectAttributes.addFlashAttribute("todoOk", "Rutina guardada correctamente.");
        return "redirect:/rutina/listado";
    }

    @GetMapping("/modificar/{idRutina}")
    public String modificar(@PathVariable Integer idRutina, Model model, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Object redireccion = validarAccesoInstructor(session, model);
        if (redireccion != null) {
            return (String) redireccion;
        }

        Instructor instructor = (Instructor) model.getAttribute("instructorSesion");
        Optional<Rutina> rutinaOpt = rutinaService.getRutina(idRutina);

        if (rutinaOpt.isEmpty() || !rutinaOpt.get().getIdInstructor().equals(instructor.getIdInstructor())) {
            redirectAttributes.addFlashAttribute("error", "La rutina no existe o no le pertenece.");
            return "redirect:/rutina/listado";
        }

        model.addAttribute("rutina", rutinaOpt.get());
        model.addAttribute("miembros", miembroService.getMiembros());
        return "rutina/formulario";
    }

    /**
     * Solo el rol INSTRUCTOR puede gestionar rutinas. Además de validar
     * la sesión y el rol, ubica al Instructor real en el modelo, para
     * usarlo en el resto de los métodos del controlador.
     */
    
    private Object validarAccesoInstructor(HttpSession session, Model model) {
        Object rolActivo = session.getAttribute("rolActivo");

        if (rolActivo == null) {
            return "redirect:/login";
        }
        if (!rolActivo.equals(RolUsuario.INSTRUCTOR.name())) {
            return "redirect:/acceso-denegado";
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioActivo");
        Optional<Instructor> instructorOpt = instructorService.getInstructorPorUsuario(usuario.getIdUsuario());

        if (instructorOpt.isEmpty()) {
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("instructorSesion", instructorOpt.get());
        return null;
    }
}