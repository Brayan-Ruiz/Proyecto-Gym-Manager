/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.service;

import Gym_Manager.com.domain.RolUsuario;
import Gym_Manager.com.domain.Usuario;
import Gym_Manager.com.repository.MiembroRepository;
import Gym_Manager.com.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final MiembroRepository miembroRepository;
    private final PagoService pagoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           MiembroRepository miembroRepository,
                           PagoService pagoService) {
        this.usuarioRepository = usuarioRepository;
        this.miembroRepository = miembroRepository;
        this.pagoService = pagoService;
    }

    @Transactional
    public ResultadoLogin validarCredenciales(String correo, String contrasena) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isEmpty()) {
            return ResultadoLogin.credencialesInvalidas();
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuario.getActivo() || !usuario.getContrasena().equals(contrasena)) {
            return ResultadoLogin.credencialesInvalidas();
        }

        // HU-03: si es un MIEMBRO, se valida si tiene el pago del mes al día.
        if (usuario.getRol() == RolUsuario.MIEMBRO) {
            var miembroOpt = miembroRepository.findByIdUsuario(usuario.getIdUsuario());
            if (miembroOpt.isPresent()) {
                boolean bloqueado = pagoService.verificarYBloquearPorPagoPendiente(miembroOpt.get());
                if (bloqueado) {
                    return ResultadoLogin.bloqueadoPorPago();
                }
            }
        }

        return ResultadoLogin.exitoso(usuario);
    }

    @Transactional
    public void save(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    /**
     * Envuelve el resultado de la validación de login, ya que ahora hay
     * más de un motivo posible de rechazo (credenciales o pago vencido).
     */
    public static class ResultadoLogin {
        private final Usuario usuario;
        private final String mensajeError;

        private ResultadoLogin(Usuario usuario, String mensajeError) {
            this.usuario = usuario;
            this.mensajeError = mensajeError;
        }

        public static ResultadoLogin exitoso(Usuario usuario) {
            return new ResultadoLogin(usuario, null);
        }

        public static ResultadoLogin credencialesInvalidas() {
            return new ResultadoLogin(null, "Correo o contraseña incorrectos.");
        }

        public static ResultadoLogin bloqueadoPorPago() {
            return new ResultadoLogin(null, "Su acceso ha sido bloqueado por pago pendiente. Contacte al administrador.");
        }

        public boolean esExitoso() {
            return usuario != null;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public String getMensajeError() {
            return mensajeError;
        }
    }
}