package cl.ipss.tarjetas.entity;

import cl.ipss.tarjetas.enums.EstadoTarjeta;
import cl.ipss.tarjetas.enums.TipoTarjeta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "tarjeta_bip")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarjetaBip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String numero;

    @Column(nullable = false, length = 100)
    private String titular;

    @Column(length = 50)
    private String alias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoTarjeta tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoTarjeta estado;

    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    @Column(name = "actualizada_en", nullable = false)
    private LocalDateTime actualizadaEn;

    @PrePersist
    void antesDeGuardar() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creadaEn = ahora;
        this.actualizadaEn = ahora;
        if (this.estado == null) {
            this.estado = EstadoTarjeta.ACTIVA;
        }
    }
    
    @PreUpdate
    void antesDeActualizar() {
        this.actualizadaEn = LocalDateTime.now();
    }
}
