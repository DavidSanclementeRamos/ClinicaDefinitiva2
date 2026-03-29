package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.CreateLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.PageLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.ReadLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.ledgerAccount.UpdateLedgerAccountDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.LedgerAccountUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.CreateAccountRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.PageAccountResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.ReadAccountResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.LedgerAccount.UpdateAccountRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.LedgerAccount.AccountRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.LedgerAccount.AccountRestWriteMapper;
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
@RequestMapping("/api/v1/accounts")
public class LedgerAccountController {

    private final LedgerAccountUseCase useCase;
    private final AccountRestReadMapper readMapper;
    private final AccountRestWriteMapper writeMapper;

    public LedgerAccountController(LedgerAccountUseCase useCase,
                             AccountRestReadMapper readMapper,
                             AccountRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadAccountResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadLedgerAccountDto dto = useCase.findById(
                LedgerAccountId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageAccountResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageLedgerAccountDto> accounts = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageAccountResponse> response = accounts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ReadAccountResponse> findByCode(
            @PathVariable String code,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadLedgerAccountDto dto = useCase.findByCode(code, requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping("/nature/{nature}")
    public ResponseEntity<Page<PageAccountResponse>> findByNature(
            @PathVariable String nature,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageLedgerAccountDto> accounts = useCase.findByNature(nature, pageable, requesterId, requesterRolId);
        Page<PageAccountResponse> response = accounts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/level/{level}")
    public ResponseEntity<Page<PageAccountResponse>> findByLevel(
            @PathVariable int level,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageLedgerAccountDto> accounts = useCase.findByLevel(level, pageable, requesterId, requesterRolId);
        Page<PageAccountResponse> response = accounts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<PageAccountResponse>> findByAccountType(
            @PathVariable String type,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageLedgerAccountDto> accounts = useCase.findByAccountType(type, pageable, requesterId, requesterRolId);
        Page<PageAccountResponse> response = accounts.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadAccountResponse> create(
            @Valid @RequestBody CreateAccountRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateLedgerAccountDto dto = writeMapper.toServiceCreate(request);
        ReadLedgerAccountDto created = useCase.createLedgerAccount(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadAccountResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAccountRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateLedgerAccountDto dto = writeMapper.toServiceUpdate(request);
        ReadLedgerAccountDto updated = useCase.updateAccountInformation(
                LedgerAccountId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.activate(LedgerAccountId.of(id), requesterId, requesterRolId);
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.inactivate(LedgerAccountId.of(id), reason, requesterId, requesterRolId);
    }
}