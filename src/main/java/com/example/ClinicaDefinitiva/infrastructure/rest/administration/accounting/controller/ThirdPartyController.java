package com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.controller;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.CreateThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.PageThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.ReadThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.thirdParties.UpdateThirdPartyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.input.ThirdPartiesUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.CreateThirdPartyRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.PageThirdPartyResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.ReadThirdPartyResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.dto.thirdParty.UpdateThirdPartyRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.thirdParty.ThirdPartyRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.accounting.mapper.thirdParty.ThirdPartyRestWriteMapper;
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
@RequestMapping("/api/v1/third-parties")
public class ThirdPartyController {

    private final ThirdPartiesUseCase useCase;
    private final ThirdPartyRestReadMapper readMapper;
    private final ThirdPartyRestWriteMapper writeMapper;

    public ThirdPartyController(ThirdPartiesUseCase useCase,
                                ThirdPartyRestReadMapper readMapper,
                                ThirdPartyRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadThirdPartyResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadThirdPartyDto dto = useCase.findById(ThirdPartiesId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageThirdPartyResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageThirdPartyDto> thirdParties = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageThirdPartyResponse> response = thirdParties.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<PageThirdPartyResponse>> findByType(
            @PathVariable String type,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageThirdPartyDto> thirdParties = useCase.findByType(type, pageable, requesterId, requesterRolId);
        Page<PageThirdPartyResponse> response = thirdParties.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/document/{documentNumber}")
    public ResponseEntity<ReadThirdPartyResponse> findByDocumentNumber(
            @PathVariable String documentNumber,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadThirdPartyDto dto = useCase.findByDocumentNumber(documentNumber, requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<PageThirdPartyResponse>> findByCompany(
            @PathVariable Long companyId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageThirdPartyDto> thirdParties = useCase.findByCompany(
                com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId.of(companyId),
                pageable, requesterId, requesterRolId);
        Page<PageThirdPartyResponse> response = thirdParties.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReadThirdPartyResponse> create(
            @Valid @RequestBody CreateThirdPartyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateThirdPartyDto dto = writeMapper.toServiceCreate(request);
        ReadThirdPartyDto created = useCase.createThirdParty(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadThirdPartyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateThirdPartyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateThirdPartyDto dto = writeMapper.toServiceUpdate(request);
        ReadThirdPartyDto updated = useCase.updateContactInformation(
                ThirdPartiesId.of(id), dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.activate(ThirdPartiesId.of(id), requesterId, requesterRolId);
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.inactivate(ThirdPartiesId.of(id), reason, requesterId, requesterRolId);
    }
}
