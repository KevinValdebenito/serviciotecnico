package com.serviciotecnico.ticket.dto;

import java.util.UUID;

public class TicketDto {
        private UUID id;
        private String title;
        private String description;
        private String status;
        private String priority;
        private UUID employeeId;
        private String clientEmail;

        public TicketDto() {
        }

        public TicketDto(UUID id, String title, String description, String status, String priority, UUID employeeId, String clientEmail) {
                this.id = id;
                this.title = title;
                this.description = description;
                this.status = status;
                this.priority = priority;
                this.employeeId = employeeId;
                this.clientEmail = clientEmail;
        }

        public UUID getId() {
                return id;
        }

        public String getTitle() {
                return title;
        }

        public String getDescription() {
                return description;
        }

        public String getStatus() {
                return status;
        }

        public String getPriority() {
                return priority;
        }

        public UUID getEmployeeId() {
                return employeeId;
        }

        public void setId(UUID id) {
                this.id = id;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public void setDescription(String description) {
                this.description = description;
        }

        public void setStatus(String status) {
                this.status = status;
        }

        public void setPriority(String priority) {
                this.priority = priority;
        }

        public void setEmployeeId(UUID employeeId) {
                this.employeeId = employeeId;
        }

        public String getClientEmail() {
                return clientEmail;
        }

        public void setClientEmail(String clientEmail) {
                this.clientEmail = clientEmail;
        }
}