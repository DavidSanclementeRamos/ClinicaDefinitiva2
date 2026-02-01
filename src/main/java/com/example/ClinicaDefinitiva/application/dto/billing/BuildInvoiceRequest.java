package com.example.ClinicaDefinitiva.application.dto.billing;

import java.time.LocalDateTime;
import java.util.List;

public class BuildInvoiceRequest {
    public String patientId;
    public String contractId;
    public String providerId;
    public List<ServiceRenderedDto> services;
    public LocalDateTime issuedAt;
    public LocalDateTime dueDate;

}
