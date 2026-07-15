package com.serviciotecnico.cotizacion.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cotizacion_detalles")
public class CotizacionDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cotizacion_id", nullable = false)
    private Cotizacion cotizacion;

    @Column(name = "repuesto_id", nullable = false)
    private UUID repuestoId;

    @Column(name = "nombre_repuesto", nullable = false, length = 150)
    private String nombreRepuesto;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    public CotizacionDetalle() {
    }

    public CotizacionDetalle(
            UUID repuestoId,
            String nombreRepuesto,
            BigDecimal precioUnitario,
            Integer cantidad,
            BigDecimal subtotal
    ) {
        this.repuestoId = repuestoId;
        this.nombreRepuesto = nombreRepuesto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public void setCotizacion(Cotizacion cotizacion) {
        this.cotizacion = cotizacion;
    }

    public UUID getRepuestoId() {
        return repuestoId;
    }

    public String getNombreRepuesto() {
        return nombreRepuesto;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
