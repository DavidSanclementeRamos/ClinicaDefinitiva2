package com.example.ClinicaDefinitiva.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InvoiceDto {
    public String id;
    public String patientId;
    public String providerId;
    public String currency;
    public String payerType;
    public LocalDateTime issuedAt;
    public LocalDateTime dueDate;
    public List<InvoiceItemDto> items;
    public String notes;
    public String status;
    public BigDecimal total;


}
