package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.CreateCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.PageCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.ReadCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyContactDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyTaxDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.CompanyUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.company.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.company.CompanyRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.company.CompanyRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyUseCase useCase;
    private final CompanyRestReadMapper readMapper;
    private final CompanyRestWriteMapper writeMapper;

    public CompanyController(CompanyUseCase useCase,
                             CompanyRestReadMapper readMapper,
                             CompanyRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadCompanyResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadCompanyDto dto = useCase.findById(CompanyId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageCompanyResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageCompanyDto> companies = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageCompanyResponse> response = companies.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PageCompanyResponse>> findByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageCompanyDto> companies = useCase.findByStatus(status, pageable, requesterId, requesterRolId);
        Page<PageCompanyResponse> response = companies.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/regime/{regime}")
    public ResponseEntity<Page<PageCompanyResponse>> findByTaxRegime(
            @PathVariable String regime,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageCompanyDto> companies = useCase.findByTaxRegime(regime, pageable, requesterId, requesterRolId);
        Page<PageCompanyResponse> response = companies.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadCompanyResponse> create(
            @Valid @RequestBody CreateCompanyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateCompanyDto dto = writeMapper.toServiceCreate(request);
        ReadCompanyDto created = useCase.createCompany(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}/contact")
    public ResponseEntity<ReadCompanyResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyContactRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateCompanyContactDto dto = writeMapper.toServiceUpdateContact(request);
        ReadCompanyDto updated = useCase.updateContactInformation(
                CompanyId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/tax")
    public ResponseEntity<ReadCompanyResponse> updateTax(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCompanyTaxRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateCompanyTaxDto dto = writeMapper.toServiceUpdateTax(request);
        ReadCompanyDto updated = useCase.updateTaxInformation(
                CompanyId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReadCompanyResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadCompanyDto updated = useCase.updateStatus(
                CompanyId.of(id),
                CompanyStatus.of(CompanyStatus.Status.valueOf(status)),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }
}