/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.controller;

import Gym_Manager.com.domain.Pago;
import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.service.MiembroService;
import Gym_Manager.com.service.PagoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Brayan Ruiz Valverde
 */
@Controller
@RequestMapping("/pago")
public class PagoController {

    private final PagoService pagoService;
    private final MiembroService miembroService;

    public PagoController(PagoService pagoService, MiembroService miembroService) {
        this.pagoService = pagoService;
        this.miembroService = miembroService;
    }

    @GetMapping("/historial/{idMiembro}")
    public String historial(@PathVariable Integer idMiembro, Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }

        var miembroOpt = miembroService.getMiembro(idMiembro);
        if (miembroOpt.isEmpty()) {
            return "redirect:/miembro/listado";
        }

        model.addAttribute("miembro", miembroOpt.get());
        model.addAttribute("pagos", pagoService.getHistorialPorMiembro(idMiembro));
        return "pago/historial";
    }

    @GetMapping("/nuevo/{idMiembro}")
    public String nuevo(@PathVariable Integer idMiembro, Model model, HttpSession session) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }

        var miembroOpt = miembroService.getMiembro(idMiembro);
        if (miembroOpt.isEmpty()) {
            return "redirect:/miembro/listado";
        }

        Pago pago = new Pago();
        pago.setIdMiembro(idMiembro);

        model.addAttribute("miembro", miembroOpt.get());
        model.addAttribute("pago", pago);
        return "pago/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Pago pago, HttpSession session, RedirectAttributes redirectAttributes) {
        String redireccion = validarAcceso(session);
        if (redireccion != null) {
            return redireccion;
        }
        pagoService.registrarPago(pago);
        redirectAttributes.addFlashAttribute("todoOk", "Pago registrado correctamente.");
        return "redirect:/pago/historial/" + pago.getIdMiembro();
    }

    /**
     * Solo el rol ADMIN puede registrar y consultar pagos.
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
