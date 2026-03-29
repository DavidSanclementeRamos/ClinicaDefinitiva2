package com.example.ClinicaDefinitiva.infrastructure.rest.payment.controller;

import com.example.ClinicaDefinitiva.application.payment.dto.CreatePaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.PaymentDto;
import com.example.ClinicaDefinitiva.application.payment.dto.RefundPaymentDto;
import com.example.ClinicaDefinitiva.application.payment.input.PaymentUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.CreatePaymentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.PaymentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.dto.RefundPaymentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.mapper.PaymentRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.payment.mapper.PaymentRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentUseCase useCase;
    private final PaymentRestReadMapper readMapper;
    private final PaymentRestWriteMapper writeMapper;

    public PaymentController(
            PaymentUseCase useCase,
            PaymentRestReadMapper readMapper,
            PaymentRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(
            @Valid @RequestBody CreatePaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreatePaymentDto dto = writeMapper.toCreateDto(request);
        PaymentDto paymentDto = useCase.processPayment(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toResponse(paymentDto));
    }

    @PostMapping("/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @Valid @RequestBody RefundPaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        RefundPaymentDto dto = writeMapper.toRefundDto(request);
        PaymentDto paymentDto = useCase.refundPayment(dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toResponse(paymentDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        PaymentDto paymentDto = useCase.findById(PaymentId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toResponse(paymentDto));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<List<PaymentResponse>> findByInvoice(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        List<PaymentDto> payments = useCase.findByInvoiceId(
                InvoiceId.of(invoiceId), requesterId, requesterRolId);

        List<PaymentResponse> response = payments.stream()
                .map(readMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/invoice/{invoiceId}/confirmed")
    public ResponseEntity<List<PaymentResponse>> findConfirmedByInvoice(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        List<PaymentDto> payments = useCase.findConfirmedPaymentsByInvoice(
                InvoiceId.of(invoiceId), requesterId, requesterRolId);

        List<PaymentResponse> response = payments.stream()
                .map(readMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PaymentResponse>> findByStatus(
            @PathVariable String status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        List<PaymentDto> payments = useCase.findByStatus(status, requesterId, requesterRolId);

        List<PaymentResponse> response = payments.stream()
                .map(readMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}