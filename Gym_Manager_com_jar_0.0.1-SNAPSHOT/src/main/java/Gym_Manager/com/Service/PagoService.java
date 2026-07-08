/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Gym_Manager.com.service;

import Gym_Manager.com.domain.EstadoMiembro;
import Gym_Manager.com.domain.EstadoPago;
import Gym_Manager.com.domain.Pago;
import Gym_Manager.com.domain.Miembro;
import Gym_Manager.com.repository.PagoRepository;
import Gym_Manager.com.repository.MiembroRepository;
import Gym_Manager.com.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagoService {

    private final PagoRepository historialPagoRepository;
    private final MiembroRepository miembroRepository;
    private final UsuarioRepository usuarioRepository;

    public PagoService(PagoRepository historialPagoRepository,
                        MiembroRepository miembroRepository,
                        UsuarioRepository usuarioRepository) {
        this.historialPagoRepository = historialPagoRepository;
        this.miembroRepository = miembroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<Pago> getHistorialPorMiembro(Integer idMiembro) {
        return historialPagoRepository.findByIdMiembroOrderByAnioDescMesDesc(idMiembro);
    }

    /**
     * HU-09: registra el pago del mes indicado. Si el pago queda marcado
     * como PAGADO, reactiva automáticamente al miembro y su cuenta de
     * acceso, en caso de que hubiesen sido bloqueados por HU-03.
     */
    
    @Transactional
    public void registrarPago(Pago pago) {
        pago.setFechaPago(pago.getEstado() == EstadoPago.PAGADO ? LocalDate.now() : null);
        historialPagoRepository.save(pago);

        if (pago.getEstado() == EstadoPago.PAGADO) {
            reactivarMiembro(pago.getIdMiembro());
        }
    }

    private void reactivarMiembro(Integer idMiembro) {
        miembroRepository.findById(idMiembro).ifPresent(miembro -> {
            if (miembro.getEstado() == EstadoMiembro.BLOQUEADO) {
                miembro.setEstado(EstadoMiembro.ACTIVO);
                miembroRepository.save(miembro);

                if (miembro.getIdUsuario() != null) {
                    usuarioRepository.findById(miembro.getIdUsuario()).ifPresent(usuario -> {
                        usuario.setActivo(true);
                        usuarioRepository.save(usuario);
                    });
                }
            }
        });
    }

    /**
     * HU-03: verifica si el miembro tiene el pago del mes actual pendiente.
     * Si es así, lo bloquea a él y a su cuenta de usuario automáticamente.
     *
     * @return true si el miembro quedó bloqueado por pago pendiente.
     */
    
    @Transactional
    public boolean verificarYBloquearPorPagoPendiente(Miembro miembro) {
        LocalDate hoy = LocalDate.now();

        return historialPagoRepository
                .findByIdMiembroAndMesAndAnio(miembro.getIdMiembro(), hoy.getMonthValue(), hoy.getYear())
                .filter(pago -> pago.getEstado() == EstadoPago.PENDIENTE)
                .map(pago -> {
                    miembro.setEstado(EstadoMiembro.BLOQUEADO);
                    miembroRepository.save(miembro);

                    if (miembro.getIdUsuario() != null) {
                        usuarioRepository.findById(miembro.getIdUsuario()).ifPresent(usuario -> {
                            usuario.setActivo(false);
                            usuarioRepository.save(usuario);
                        });
                    }
                    return true;
                })
                .orElse(false);
    }
}
