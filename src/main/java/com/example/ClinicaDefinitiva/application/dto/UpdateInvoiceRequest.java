package com.example.ClinicaDefinitiva.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UpdateInvoiceRequest {
    public String invoiceId;               // id expuesto como String (UUID o similar)
    public LocalDateTime issuedAt;         // opcional: si null se mantiene el valor actual
    public LocalDateTime dueDate;          // opcional: si null se recalcula según política
    public String notes;                   // opcional
    public Long contractId;                // opcional
    public List<ServiceRenderedDto> services; // lista completa para reemplazar items

    // opcionales: permitir actualizar payer/status según reglas de negocio
    public String payer;
    public String status;

    public UpdateInvoiceRequest() {
    }

    public UpdateInvoiceRequest(Long contractId, LocalDateTime dueDate, String invoiceId, LocalDateTime issuedAt, String notes, String payer, List<ServiceRenderedDto> services, String status) {
        this.contractId = contractId;
        this.dueDate = dueDate;
        this.invoiceId = invoiceId;
        this.issuedAt = issuedAt;
        this.notes = notes;
        this.payer = payer;
        this.services = services;
        this.status = status;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setServices(List<ServiceRenderedDto> services) {
        this.services = services;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
