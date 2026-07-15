package com.serviciotecnico.diagnostico.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "diagnosticos")
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ticket_id", nullable = false)
    private UUID ticketId;

    @Column(name = "empleado_id", nullable = false)
    private UUID empleadoId;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public Diagnostico() {
    }

    public Diagnostico(UUID ticketId, UUID empleadoId, String descripcion) {
        this.ticketId = ticketId;
        this.empleadoId = empleadoId;
        this.descripcion = descripcion;
    }

    @PrePersist
    protected void onCreate() {
        this.fecha = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(UUID empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}