package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.CreateContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.PageContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.ReadContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.contract.UpdateContractDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.ContractUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.contract.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.contract.ContractRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.contract.ContractRestWriteMapper;
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
@RequestMapping("/api/v1/contracts")
public class ContractController {

    private final ContractUseCase useCase;
    private final ContractRestReadMapper readMapper;
    private final ContractRestWriteMapper writeMapper;

    public ContractController(ContractUseCase useCase,
                              ContractRestReadMapper readMapper,
                              ContractRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadContractResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadContractDto dto = useCase.findById(ContractId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageContractResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageContractDto> contracts = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageContractResponse> response = contracts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<PageContractResponse>> findByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageContractDto> contracts = useCase.findByCompany(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId.of(companyId),
                pageable, requesterId, requesterRolId);
        Page<PageContractResponse> response = contracts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/third-party/{thirdPartyId}")
    public ResponseEntity<Page<PageContractResponse>> findByThirdParty(
            @PathVariable Long thirdPartyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageContractDto> contracts = useCase.findByThirdParty(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId.of(thirdPartyId),
                pageable, requesterId, requesterRolId);
        Page<PageContractResponse> response = contracts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PageContractResponse>> findByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageContractDto> contracts = useCase.findByStatus(status, pageable, requesterId, requesterRolId);
        Page<PageContractResponse> response = contracts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiring")
    public ResponseEntity<Page<PageContractResponse>> findExpiringSoon(
            @RequestParam(defaultValue = "30") int days,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageContractDto> contracts = useCase.findExpiringSoon(days, pageable, requesterId, requesterRolId);
        Page<PageContractResponse> response = contracts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadContractResponse> create(
            @Valid @RequestBody CreateContractRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateContractDto dto = writeMapper.toServiceCreate(request);
        ReadContractDto created = useCase.createContract(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadContractResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateContractRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateContractDto dto = writeMapper.toServiceUpdate(request);
        ReadContractDto updated = useCase.updateInformation(
                ContractId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/extend")
    public ResponseEntity<ReadContractResponse> extend(
            @PathVariable Long id,
            @Valid @RequestBody ExtendContractRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadContractDto updated = useCase.extendContract(
                ContractId.of(id), request.newEndDate(), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.suspend(ContractId.of(id), reason, requesterId, requesterRolId);
    }

    @PostMapping("/{id}/reactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reactivate(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.reactivate(ContractId.of(id), requesterId, requesterRolId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void terminate(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.terminate(ContractId.of(id), reason, requesterId, requesterRolId);
    }
}
