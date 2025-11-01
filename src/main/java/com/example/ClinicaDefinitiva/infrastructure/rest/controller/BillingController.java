package com.example.ClinicaDefinitiva.infrastructure.rest.controller;

import com.example.ClinicaDefinitiva.application.dto.BuildInvoiceRequest;
import com.example.ClinicaDefinitiva.application.dto.InvoiceDto;
import com.example.ClinicaDefinitiva.application.usecase.BillingUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/v1/invoices")
public class BillingController {

    private final BillingUseCase billingUseCase;

    public BillingController(BillingUseCase billingUseCase) {
        this.billingUseCase = billingUseCase;
    }

    @PostMapping
    public ResponseEntity<InvoiceDto> buildInvoice(@Valid @RequestBody BuildInvoiceRequest request) {
        InvoiceDto invoice = billingUseCase.buildInvoice(request);
        return ResponseEntity.ok(invoice);
    }
}

