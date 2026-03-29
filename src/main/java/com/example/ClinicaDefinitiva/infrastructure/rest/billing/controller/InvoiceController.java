package com.example.ClinicaDefinitiva.infrastructure.rest.billing.controller;


import com.example.ClinicaDefinitiva.application.billing.dto.invoice.AddInvoiceItemDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateInstitutionalInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.CreateParticularInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.PageInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.dto.invoice.ReadInvoiceDto;
import com.example.ClinicaDefinitiva.application.billing.input.InvoiceUseCase;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.dto.invoice.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.mapper.invoice.InvoiceRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.billing.mapper.invoice.InvoiceRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST Controller for Invoices (Billing).
 *
 * Provides endpoints for managing dental clinic invoices.
 * All operations require authentication and proper authorization.
 */
@RestController
@Validated
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceUseCase useCase;
    private final InvoiceRestReadMapper readMapper;
    private final InvoiceRestWriteMapper writeMapper;

    public InvoiceController(InvoiceUseCase useCase,
                             InvoiceRestReadMapper readMapper,
                             InvoiceRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadInvoiceDto dto = useCase.findById(
                InvoiceId.of(id),
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageInvoiceResponse>> findAll(
            @PageableDefault(size = 20, sort = "updatedAt,desc") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageInvoiceDto> invoices = useCase.findAll(
                pageable,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(invoices.map(readMapper::toPageRest));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<PageInvoiceResponse>> findByPatient(
            @PathVariable Long patientId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageInvoiceDto> invoices = useCase.findByPatient(
              PatientId.of(  patientId),
                pageable,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(invoices.map(readMapper::toPageRest));
    }

    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<Page<PageInvoiceResponse>> findByDentist(
            @PathVariable Long dentistId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageInvoiceDto> invoices = useCase.findByDentist(
              DentistId.of(  dentistId),
                pageable,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(invoices.map(readMapper::toPageRest));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PageInvoiceResponse>> findByStatus(
            @PathVariable InvoiceStatus.Status status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageInvoiceDto> invoices = useCase.findByStatus(
                status,
                pageable,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(invoices.map(readMapper::toPageRest));
    }

    @GetMapping("/number/{invoiceNumber}")
    public ResponseEntity<ReadInvoiceResponse> findByNumber(
            @PathVariable String invoiceNumber,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadInvoiceDto dto = useCase.findByNumber(
                invoiceNumber,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<PageInvoiceResponse>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageInvoiceDto> invoices = useCase.findByDateRange(
                startDate,
                endDate,
                pageable,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );
        return ResponseEntity.ok(invoices.map(readMapper::toPageRest));
    }

    @PostMapping("/particular")
    public ResponseEntity<ReadInvoiceResponse> createParticular(
            @Valid @RequestBody CreateParticularInvoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CreateParticularInvoiceDto dto = writeMapper.toServiceCreateParticular(request);
        ReadInvoiceDto created = useCase.createParticular(
                dto,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PostMapping("/institutional")
    public ResponseEntity<ReadInvoiceResponse> createInstitutional(
            @Valid @RequestBody CreateInstitutionalInvoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CreateInstitutionalInvoiceDto dto = writeMapper.toServiceCreateInstitutional(request);
        ReadInvoiceDto created = useCase.createInstitutional(
                dto,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ReadInvoiceResponse> addItem(
            @PathVariable Long id,
            @Valid @RequestBody AddInvoiceItemRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        AddInvoiceItemDto dto = writeMapper.toServiceAddItem(request);
        ReadInvoiceDto updated = useCase.addItem(
                InvoiceId.of(id),
                dto,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/emit")
    public ResponseEntity<ReadInvoiceResponse> emit(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadInvoiceDto emitted = useCase.emit(
                InvoiceId.of(id),
                userDetails.getId(),
                userDetails.getActiveRolId()
        );

        return ResponseEntity.ok(readMapper.toRest(emitted));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReadInvoiceResponse> cancel(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadInvoiceDto cancelled = useCase.cancel(
                InvoiceId.of(id),
                reason,
                userDetails.getId(),
                userDetails.getActiveRolId()
        );

        return ResponseEntity.ok(readMapper.toRest(cancelled));
    }

   
}
