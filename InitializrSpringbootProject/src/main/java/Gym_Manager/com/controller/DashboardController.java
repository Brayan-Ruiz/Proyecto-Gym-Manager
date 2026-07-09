/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.RolUsuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Controller
public class DashboardController {

    @GetMapping("/dashboard/admin")
    public String dashboardAdmin(HttpSession session) {
        String redireccion = validarAcceso(session, RolUsuario.ADMIN);
        if (redireccion != null) {
            return redireccion;
        }
        return "dashboard/admin";
    }

    @GetMapping("/dashboard/instructor")
    public String dashboardInstructor(HttpSession session) {
        String redireccion = validarAcceso(session, RolUsuario.INSTRUCTOR);
        if (redireccion != null) {
            return redireccion;
        }
        return "dashboard/instructor";
    }

    @GetMapping("/dashboard/miembro")
    public String dashboardMiembro(HttpSession session) {
        String redireccion = validarAcceso(session, RolUsuario.MIEMBRO);
        if (redireccion != null) {
            return redireccion;
        }
        return "dashboard/miembro";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "Sesion/acceso-denegado";
    }

    private String validarAcceso(HttpSession session, RolUsuario rolRequerido) {
        Object rolActivo = session.getAttribute("rolActivo");

        if (rolActivo == null) {
            return "redirect:/login";
        }

        if (!rolActivo.equals(rolRequerido.name())) {
            return "redirect:/acceso-denegado";
        }

        return null;
    }
}
