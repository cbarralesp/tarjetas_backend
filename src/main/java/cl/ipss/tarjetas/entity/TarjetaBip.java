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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTITY (el "Model" de MVC): representa la tabla tarjeta_bip.
 * Java puro: constructor vacío, getters y setters escritos a mano.
 */
@Entity
@Table(name = "tarjeta_bip")
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

    /** Constructor vacío: JPA lo exige para crear la entidad al leer de la base. */
    public TarjetaBip() {
    }

    // ---------- getters ----------
    public Long getId() {
        return id;
    }

    public String getNumero() {
        return numero;
    }

    public String getTitular() {
        return titular;
    }

    public String getAlias() {
        return alias;
    }

    public TipoTarjeta getTipo() {
        return tipo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public EstadoTarjeta getEstado() {
        return estado;
    }

    public LocalDateTime getCreadaEn() {
        return creadaEn;
    }

    public LocalDateTime getActualizadaEn() {
        return actualizadaEn;
    }

    // ---------- setters (id y fechas los maneja JPA, por eso no tienen) ----------
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setTipo(TipoTarjeta tipo) {
        this.tipo = tipo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setEstado(EstadoTarjeta estado) {
        this.estado = estado;
    }

    /** Se ejecuta automáticamente justo antes del INSERT. */
    @PrePersist
    void antesDeGuardar() {
        LocalDateTime ahora = LocalDateTime.now();
        this.creadaEn = ahora;
        this.actualizadaEn = ahora;
        if (this.estado == null) {
            this.estado = EstadoTarjeta.ACTIVA;
        }
    }

    /** Se ejecuta automáticamente justo antes del UPDATE. */
    @PreUpdate
    void antesDeActualizar() {
        this.actualizadaEn = LocalDateTime.now();
    }
}
