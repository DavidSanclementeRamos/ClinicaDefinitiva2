package com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.controller;

import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.PageAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.dto.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.administration.authorization.input.UserRolAssignmentUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.advice.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.CreateAssignmentPermanentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.CreateAssignmentTemporaryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.dto.UserRolAssignment.ReadAssignmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.userRolAssignment.UserRolAssignmentReadRestMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.authorization.mapper.userRolAssignment.UserRolAssignmentWriteRestMapper;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/v1/user-assignments")
public class UserRolAssignmentController {

    private final UserRolAssignmentReadRestMapper readMapper;
    private final UserRolAssignmentWriteRestMapper writeMapper;
    private final UserRolAssignmentUseCase useCase;

    public UserRolAssignmentController(UserRolAssignmentReadRestMapper readMapper,
                                       UserRolAssignmentWriteRestMapper writeMapper,
                                       UserRolAssignmentUseCase useCase) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.useCase = useCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadAssignmentResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        ReadAssignmentDto dto = useCase.findById(UserRolAssignmentId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PageResponse<ReadAssignmentResponse>> findByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        Page<ReadAssignmentDto> page = useCase.findByUserId(UserIdentityId.from(userId), requesterId, requesterRolId,pageable);
        Page<ReadAssignmentResponse> responsePage = page.map(readMapper::toRest);
        return ResponseEntity.ok(new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        ));
    }

    @GetMapping("/user/{userId}/rol/{rolId}")
    public ResponseEntity<PageResponse<ReadAssignmentResponse>> findByUserIdAndRolId(
            @PathVariable Long userId,
            @PathVariable Long rolId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        Page<ReadAssignmentDto> page = useCase.findByUserIdAndRolId(
                UserIdentityId.from(userId), RolId.of(rolId), requesterId, requesterRolId,pageable);
        Page<ReadAssignmentResponse> responsePage = page.map(readMapper::toRest);
        return ResponseEntity.ok(new PageResponse<>(
                responsePage.getContent(),
                responsePage.getNumber(),
                responsePage.getSize(),
                responsePage.getTotalElements(),
                responsePage.getTotalPages(),
                responsePage.isLast()
        ));
    }

    @GetMapping("/user/{userId}/primary")
    public ResponseEntity<ReadAssignmentResponse> findByUserIdAndIsPrimary(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        ReadAssignmentDto dto = useCase.findByUserIdAndIsPrimary(UserIdentityId.from(userId), true, requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @PostMapping("/permanent")
    public ResponseEntity<ReadAssignmentResponse> savePermanent(
            @Valid @RequestBody CreateAssignmentPermanentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        CreateAssignmentPermanentDto dto = writeMapper.toServicePermanent(request);
        ReadAssignmentDto saved = useCase.savePermanent(dto, requesterId, requesterRolId);
        return ResponseEntity.status(HttpStatus.CREATED).body(readMapper.toRest(saved));
    }

    @PostMapping("/temporary")
    public ResponseEntity<ReadAssignmentResponse> saveTemporary(
            @Valid @RequestBody CreateAssignmentTemporaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        CreateAssignmentTemporaryDto dto = writeMapper.toServiceTemporary(request);
        ReadAssignmentDto saved = useCase.saveTemporary(dto, requesterId, requesterRolId);
        return ResponseEntity.status(HttpStatus.CREATED).body(readMapper.toRest(saved));
    }

    @GetMapping("/{id}/active-at")
    public ResponseEntity<Boolean> isActiveAt(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        boolean result = useCase.isActiveAt(UserRolAssignmentId.of(id), date, requesterId, requesterRolId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isCurrentlyActive(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        boolean result = useCase.isCurrentlyActive(UserRolAssignmentId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/extend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void extend(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate newValidTo,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        useCase.extend(UserRolAssignmentId.of(id), newValidTo, requesterId, requesterRolId);
    }

    @DeleteMapping("/user/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAllRoles(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        useCase.revokeAllRol(UserIdentityId.from(userId), requesterId, requesterRolId);
    }

    @DeleteMapping("/user/{userId}/roles/{rolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeRole(
            @PathVariable Long userId,
            @PathVariable Long rolId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        useCase.revokeRol(UserIdentityId.from(userId), RolId.of(rolId), requesterId, requesterRolId);
    }

    @PatchMapping("/{id}/primary")
    public ResponseEntity<ReadAssignmentResponse> updatePrimary(
            @PathVariable Long id,
            @RequestParam boolean isPrimary,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        ReadAssignmentDto updated = useCase.updatePrimary(UserRolAssignmentId.of(id), isPrimary, requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssignment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();
        useCase.deleteAssignment(UserRolAssignmentId.of(id), requesterId, requesterRolId);
    }
}