package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.AddJournalEntryLineDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.CreateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.PageJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.ReadJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.journalEntry.UpdateJournalEntryDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.JournalEntryUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.AddJournalEntryLineRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.CreateJournalEntryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.PageJournalEntryResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.ReadJournalEntryResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.ReversalJournalEntryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.journalEntry.UpdateJournalEntryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.journalEntry.JournalEntryRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.journalEntry.JournalEntryRestWriteMapper;
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

@RestController
@Validated
@RequestMapping("/api/v1/journal-entries")
public class JournalEntryController {

    private final JournalEntryUseCase useCase;
    private final JournalEntryRestReadMapper readMapper;
    private final JournalEntryRestWriteMapper writeMapper;

    public JournalEntryController(JournalEntryUseCase useCase,
                                  JournalEntryRestReadMapper readMapper,
                                  JournalEntryRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadJournalEntryResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadJournalEntryDto dto = useCase.findById(JournalEntryId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageJournalEntryResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageJournalEntryDto> entries = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<PageJournalEntryResponse>> findByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageJournalEntryDto> entries = useCase.findByCompany(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId.of(companyId),
                pageable, requesterId, requesterRolId);
        Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/date-range")
    public ResponseEntity<Page<PageJournalEntryResponse>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageJournalEntryDto> entries = useCase.findByDateRange(start, end, pageable, requesterId, requesterRolId);
        Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<PageJournalEntryResponse>> findByAccount(
            @PathVariable Long accountId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageJournalEntryDto> entries = useCase.findByAccount(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId.of(accountId),
                pageable, requesterId, requesterRolId);
        Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/third-party/{thirdPartyId}")
    public ResponseEntity<Page<PageJournalEntryResponse>> findByThirdParty(
            @PathVariable Long thirdPartyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageJournalEntryDto> entries = useCase.findByThirdParty(
              ThirdPartiesId.of(thirdPartyId),
                pageable, requesterId, requesterRolId);
        Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unposted")
    public ResponseEntity<ReadJournalEntryResponse> post(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadJournalEntryDto entries = useCase.post(JournalEntryId.of(id), requesterId, requesterRolId);
        //Page<PageJournalEntryResponse> response = entries.map(readMapper::toPageRest);

        return ResponseEntity.ok(readMapper.toRest(entries));
    }

    @PostMapping
    public ResponseEntity<ReadJournalEntryResponse> create(
            
            @Valid @RequestBody CreateJournalEntryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateJournalEntryDto dto = writeMapper.toServiceCreate(request);
        ReadJournalEntryDto created = useCase.createJournalEntry(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PostMapping("/{id}/lines")
    public ResponseEntity<ReadJournalEntryResponse> addLine(
            @PathVariable Long id,
            @Valid @RequestBody AddJournalEntryLineRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        AddJournalEntryLineDto dto = writeMapper.toServiceAddLine(request);
        ReadJournalEntryDto updated = useCase.addLine(JournalEntryId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @DeleteMapping("/{id}/lines/{lineIndex}")
    public ResponseEntity<ReadJournalEntryResponse> removeLine(
            @PathVariable Long id,
            @PathVariable int lineIndex,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadJournalEntryDto updated = useCase.removeLine(JournalEntryId.of(id), lineIndex, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadJournalEntryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateJournalEntryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateJournalEntryDto dto = writeMapper.toServiceUpdate(request);
        ReadJournalEntryDto updated = useCase.updateInformation(JournalEntryId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/post")
    public ResponseEntity<ReadJournalEntryResponse> post(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadJournalEntryDto posted = useCase.post(JournalEntryId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(posted));
    }

    @PostMapping("/{id}/reverse")
    public ResponseEntity<ReadJournalEntryResponse> reverse(
            @PathVariable Long id,
            @Valid @RequestBody ReversalJournalEntryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadJournalEntryDto reversed = useCase.registerRverse(JournalEntryId.of(id), request.reason(), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(reversed));
    }
}
