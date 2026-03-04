package com.example.ClinicaDefinitiva.infrastructure.rest.controller.billing;


import com.example.ClinicaDefinitiva.application.dto.billing.rate.CreateRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.PageRateDto;
import com.example.ClinicaDefinitiva.application.dto.billing.rate.ReadRateDto;
import com.example.ClinicaDefinitiva.application.portsInput.billing.RateUseCase;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.billing.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing.RateRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.billing.RateRestWriteMapper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping("/api/v1/rates")
public class RateController {

    private final RateUseCase useCase;
    private final RateRestReadMapper readMapper;
    private final RateRestWriteMapper writeMapper;

    public RateController(RateUseCase useCase,
                          RateRestReadMapper readMapper,
                          RateRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadRateResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadRateDto dto = useCase.findById(RateId.of(id), userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageRateResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageRateDto> rates = useCase.findAll(pageable, userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(rates.map(readMapper::toPageRest));
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<Page<PageRateResponse>> findByService(
            @PathVariable Long serviceId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageRateDto> rates = useCase.findByService(serviceId, pageable, userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(rates.map(readMapper::toPageRest));
    }

    @GetMapping("/payer-type/{payerType}")
    public ResponseEntity<Page<PageRateResponse>> findByPayerType(
            @PathVariable Rate.PayerType payerType,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageRateDto> rates = useCase.findByPayerType(payerType, pageable, userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(rates.map(readMapper::toPageRest));
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<Page<PageRateResponse>> findByContract(
            @PathVariable Long contractId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageRateDto> rates = useCase.findByContract(contractId, pageable, userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(rates.map(readMapper::toPageRest));
    }

    @GetMapping("/active")
    public ResponseEntity<ReadRateResponse> findActiveRateForService(
            @RequestParam Long serviceId,
            @RequestParam Rate.PayerType payerType,
            @RequestParam(required = false) Long contractId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadRateDto dto = useCase.findActiveRateForService(serviceId, payerType, contractId,
                userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping("/currently-valid")
    public ResponseEntity<Page<PageRateResponse>> findCurrentlyValid(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Page<PageRateDto> rates = useCase.findCurrentlyValid(pageable, userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(rates.map(readMapper::toPageRest));
    }

    @PostMapping
    public ResponseEntity<ReadRateResponse> create(
            @Valid @RequestBody CreateRateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        CreateRateDto dto = writeMapper.toServiceCreate(request);
        ReadRateDto created = useCase.create(dto, userDetails.getId(), userDetails.getActiveRolId());

        return ResponseEntity.status(HttpStatus.CREATED).body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}/update-amount")
    public ResponseEntity<ReadRateResponse> updateAmount(
            @PathVariable Long id,
            @RequestParam BigDecimal newAmount,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime validFrom,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadRateDto updated = useCase.updateAmount(RateId.of(id), newAmount, validFrom,
                userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/end-validity")
    public ResponseEntity<ReadRateResponse> endValidity(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        ReadRateDto updated = useCase.endValidity(RateId.of(id), endDate,
                userDetails.getId(), userDetails.getActiveRolId());
        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        useCase.deactivate(RateId.of(id), userDetails.getId(), userDetails.getActiveRolId());
    }
}
