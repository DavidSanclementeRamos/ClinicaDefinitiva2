package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.CreateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.PageAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.PeriodDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.ReadAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.administrativeReport.UpdateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.AdministrativeReportUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AddAttachmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AddIndicatorRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.AddJournalEntryReferenceRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.CreateAdministrativeReportRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.PageAdministrativeReportResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.ReadAdministrativeReportResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.administrativeReport.UpdateAdministrativeReportRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.administrativeReport.AdministrativeReportRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.administrativeReport.AdministrativeReportRestWriteMapper;
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
@RequestMapping("/api/v1/administrative-reports")
public class AdministrativeReportController {

    private final AdministrativeReportUseCase useCase;
    private final AdministrativeReportRestReadMapper readMapper;
    private final AdministrativeReportRestWriteMapper writeMapper;

    public AdministrativeReportController(AdministrativeReportUseCase useCase,
                                          AdministrativeReportRestReadMapper readMapper,
                                          AdministrativeReportRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadAdministrativeReportResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto dto = useCase.findById(AdministrativeReportId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageAdministrativeReportResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageAdministrativeReportDto> reports = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageAdministrativeReportResponse> response = reports.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/period")
    public ResponseEntity<Page<PageAdministrativeReportResponse>> findByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageAdministrativeReportDto> reports = useCase.findByPeriod(
                new PeriodDto(start, end), pageable, requesterId, requesterRolId);
        Page<PageAdministrativeReportResponse> response = reports.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PageAdministrativeReportResponse>> findByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageAdministrativeReportDto> reports = useCase.findByStatus(status, pageable, requesterId, requesterRolId);
        Page<PageAdministrativeReportResponse> response = reports.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/creator/{creatorId}")
    public ResponseEntity<Page<PageAdministrativeReportResponse>> findByCreator(
            @PathVariable Long creatorId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageAdministrativeReportDto> reports = useCase.findByCreator(
                UserIdentityId.from(creatorId), pageable, requesterId, requesterRolId);
        Page<PageAdministrativeReportResponse> response = reports.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadAdministrativeReportResponse> create(
            @Valid @RequestBody CreateAdministrativeReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateAdministrativeReportDto dto = writeMapper.toServiceCreate(request);
        ReadAdministrativeReportDto created = useCase.createReport(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PostMapping("/{id}/journal-entries")
    public ResponseEntity<ReadAdministrativeReportResponse> addJournalEntryReference(
            @PathVariable Long id,
            @Valid @RequestBody AddJournalEntryReferenceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.addJournalEntryReference(
                AdministrativeReportId.of(id),
                JournalEntryId.of(request.journalEntryId()),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @DeleteMapping("/{id}/journal-entries/{entryId}")
    public ResponseEntity<ReadAdministrativeReportResponse> removeJournalEntryReference(
            @PathVariable Long id,
            @PathVariable Long entryId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.removeJournalEntryReference(
                AdministrativeReportId.of(id),
                JournalEntryId.of(entryId),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/indicators")
    public ResponseEntity<ReadAdministrativeReportResponse> addIndicator(
            @PathVariable Long id,
            @Valid @RequestBody AddIndicatorRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.addIndicator(
                AdministrativeReportId.of(id),
                writeMapper.toIndicatorDto(request),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @DeleteMapping("/{id}/indicators")
    public ResponseEntity<ReadAdministrativeReportResponse> removeIndicator(
            @PathVariable Long id,
            @Valid @RequestBody AddIndicatorRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.removeIndicator(
                AdministrativeReportId.of(id),
                writeMapper.toIndicatorDto(request),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/attachments")
    public ResponseEntity<ReadAdministrativeReportResponse> addAttachment(
            @PathVariable Long id,
            @Valid @RequestBody AddAttachmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.addAttachment(
                AdministrativeReportId.of(id),
                writeMapper.toDocumentDto(request),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @DeleteMapping("/{id}/attachments")
    public ResponseEntity<ReadAdministrativeReportResponse> removeAttachment(
            @PathVariable Long id,
            @Valid @RequestBody AddAttachmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.removeAttachment(
                AdministrativeReportId.of(id),
                writeMapper.toDocumentDto(request),
                requesterId,
                requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadAdministrativeReportResponse> updateInformation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdministrativeReportRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateAdministrativeReportDto dto = writeMapper.toServiceUpdate(request);
        ReadAdministrativeReportDto updated = useCase.updateInformation(
                AdministrativeReportId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<ReadAdministrativeReportResponse> submitForReview(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto updated = useCase.submitForReview(
                AdministrativeReportId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ReadAdministrativeReportResponse> approve(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto approved = useCase.approve(
                AdministrativeReportId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(approved));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<ReadAdministrativeReportResponse> reject(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAdministrativeReportDto rejected = useCase.reject(
                AdministrativeReportId.of(id), reason, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(rejected));
    }

    @PostMapping("/{id}/archive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.archive(AdministrativeReportId.of(id), requesterId, requesterRolId);
    }

    @PostMapping("/{id}/unarchive")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unarchive(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.unarchive(AdministrativeReportId.of(id), requesterId, requesterRolId);
    }
}
