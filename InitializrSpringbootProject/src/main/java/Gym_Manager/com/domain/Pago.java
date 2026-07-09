package Gym_Manager.com.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author Brayan Ruiz Valverde
 */

@Data
@Entity
@Table(name = "historial_pago")
public class Pago implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idPago;

    @Column(name = "id_miembro", nullable = false)
    @NotNull
    private Integer idMiembro;

    @Min(1) @Max(12)
    @Column(nullable = false)
    @NotNull
    private Integer mes;

    @Column(nullable = false)
    @NotNull
    private Integer anio;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(nullable = false)
    @NotNull
    private Double monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPago estado = EstadoPago.PENDIENTE;
}
