/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.Usuario;
import Gym_Manager.com.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
                                 @RequestParam String contrasena,
                                 HttpSession session,
                                 Model model) {
        UsuarioService.ResultadoLogin resultado = usuarioService.validarCredenciales(correo, contrasena);

        if (!resultado.esExitoso()) {
            model.addAttribute("error", resultado.getMensajeError());
            return "login";
        }

        Usuario usuario = resultado.getUsuario();
        session.setAttribute("usuarioActivo", usuario);
        session.setAttribute("rolActivo", usuario.getRol().name());

        return switch (usuario.getRol()) {
            case ADMIN -> "redirect:/dashboard/admin";
            case INSTRUCTOR -> "redirect:/dashboard/instructor";
            case MIEMBRO -> "redirect:/dashboard/miembro";
        };
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}