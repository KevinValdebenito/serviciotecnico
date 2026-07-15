package com.serviciotecnico.cotizacion.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "cotizaciones")
public class Cotizacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "mano_obra", nullable = false, precision = 12, scale = 2)
    private BigDecimal manoObra;

    @Column(name = "costo_repuestos", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoRepuestos;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal neto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal iva;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Column(nullable = false, length = 30)
    private String estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(
            mappedBy = "cotizacion",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CotizacionDetalle> detalles = new ArrayList<>();

    public Cotizacion() {
    }

    @PrePersist
    void antesDeCrear() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void antesDeActualizar() {
        fechaActualizacion = LocalDateTime.now();
    }

    public void reemplazarDetalles(List<CotizacionDetalle> nuevosDetalles) {
        detalles.clear();
        nuevosDetalles.forEach(this::agregarDetalle);
    }

    public void agregarDetalle(CotizacionDetalle detalle) {
        detalle.setCotizacion(this);
        detalles.add(detalle);
    }

    public UUID getId() {
        return id;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getManoObra() {
        return manoObra;
    }

    public void setManoObra(BigDecimal manoObra) {
        this.manoObra = manoObra;
    }

    public BigDecimal getCostoRepuestos() {
        return costoRepuestos;
    }

    public void setCostoRepuestos(BigDecimal costoRepuestos) {
        this.costoRepuestos = costoRepuestos;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getNeto() {
        return neto;
    }

    public void setNeto(BigDecimal neto) {
        this.neto = neto;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public List<CotizacionDetalle> getDetalles() {
        return detalles;
    }
}
