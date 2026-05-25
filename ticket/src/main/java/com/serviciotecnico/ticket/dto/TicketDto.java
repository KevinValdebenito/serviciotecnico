package com.serviciotecnico.ticket.dto;

import java.util.UUID;

public class TicketDto {
        private UUID id;
        private String titulo;
        private String descripcion;
        private String estado;
        private String prioridad;
        private UUID empleadoId;

        public TicketDto() {
        }

        public TicketDto(UUID id, String titulo, String descripcion, String estado, String prioridad, UUID empleadoId) {
                this.id = id;
                this.titulo = titulo;
                this.descripcion = descripcion;
                this.estado = estado;
                this.prioridad = prioridad;
                this.empleadoId = empleadoId;
        }

        public UUID getId() {
                return id;
        }

        public String getTitulo() {
                return titulo;
        }

        public String getDescripcion() {
                return descripcion;
        }

        public String getEstado() {
                return estado;
        }

        public String getPrioridad() {
                return prioridad;
        }

        public UUID getEmpleadoId() {
                return empleadoId;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public void setTitulo(String titulo) {
                this.titulo = titulo;
        }

        public void setDescripcion(String descripcion) {
                this.descripcion = descripcion;
        }

        public void setEstado(String estado) {
                this.estado = estado;
        }

        public void setPrioridad(String prioridad) {
                this.prioridad = prioridad;
        }

        public void setEmpleadoId(UUID empleadoId) {
                this.empleadoId = empleadoId;
        }
}