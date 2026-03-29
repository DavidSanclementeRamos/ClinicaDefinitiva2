package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.OpeningBalanceUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.CreateOpeningBalanceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.PageOpeningBalanceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.openingBalance.ReadOpeningBalanceResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.openingBalance.OpeningBalanceRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.openingBalance.OpeningBalanceRestWriteMapper;
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
@RequestMapping("/api/v1/opening-balances")
public class OpeningBalanceController {

    private final OpeningBalanceUseCase useCase;
    private final OpeningBalanceRestReadMapper readMapper;
    private final OpeningBalanceRestWriteMapper writeMapper;

    public OpeningBalanceController(OpeningBalanceUseCase useCase,
                                    OpeningBalanceRestReadMapper readMapper,
                                    OpeningBalanceRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadOpeningBalanceResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadOpeningBalanceDto dto = useCase.findById(OpeningBalanceId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageOpeningBalanceResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageOpeningBalanceDto> balances = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageOpeningBalanceResponse> response = balances.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<PageOpeningBalanceResponse>> findByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageOpeningBalanceDto> balances = useCase.findByCompany(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId.of(companyId),
                pageable, requesterId, requesterRolId);
        Page<PageOpeningBalanceResponse> response = balances.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<PageOpeningBalanceResponse>> findByAccount(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageOpeningBalanceDto> balances = useCase.findByAccount(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId.of(accountId),
                pageable, requesterId, requesterRolId);
        Page<PageOpeningBalanceResponse> response = balances.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadOpeningBalanceResponse> create(
            @Valid @RequestBody CreateOpeningBalanceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateOpeningBalanceDto dto = writeMapper.toServiceCreate(request);
        ReadOpeningBalanceDto created = useCase.createOpeningBalance(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.delete(OpeningBalanceId.of(id), requesterId, requesterRolId);
    }
}
