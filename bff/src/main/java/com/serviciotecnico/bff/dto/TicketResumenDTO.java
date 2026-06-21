package com.serviciotecnico.bff.dto;

import java.util.UUID;

public class TicketResumenDTO {

    private UUID idTicket;
    private String titulo;
    private String estado;

    private String nombreCliente;
    private String telefonoCliente;

    private String nombreTecnico;

    public TicketResumenDTO() {}

    public UUID getIdTicket() {
        return idTicket;
    }   

    public void setIdTicket(UUID idTicket) {
        this.idTicket = idTicket;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo (String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }
}
