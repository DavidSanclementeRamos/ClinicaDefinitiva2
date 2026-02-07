package com.example.ClinicaDefinitiva.infrastructure.rest.controller.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentPermanentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.CreateAssignmentTemporaryDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.UserRolAssignment.ReadAssignmentDto;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.UserRolAssignmentUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.CreateAssignmentPermanentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.CreateAssignmentTemporaryRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.UserRolAssignment.ReadAssignmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authorization.userRolAssignment.UserRolAssignmentReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authorization.userRolAssignment.UserRolAssignmentWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;


@RestController
@Validated
@RequestMapping("/api/v1/user-assignments")
public class UserRolAssignmentController {

    private final UserRolAssignmentReadMapper readMapper;
    private final UserRolAssignmentWriteMapper writeMapper;
    private final UserRolAssignmentUseCase useCase;

    public UserRolAssignmentController(UserRolAssignmentReadMapper readMapper,
                                       UserRolAssignmentWriteMapper writeMapper,
                                       UserRolAssignmentUseCase useCase) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.useCase = useCase;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReadAssignmentResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findById(UserRolAssignmentId.of(id), requesterId, requesterRolId)
                .map(readMapper::toRest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadAssignmentResponse>> findByUserId(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        List<ReadAssignmentDto> assignments = useCase.findByUserId(UserId.from(userId), requesterId, requesterRolId);
        return ResponseEntity.ok(
                assignments.stream()
                        .map(readMapper::toRest)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/user/{userId}/rol/{rolId}")
    public ResponseEntity<ReadAssignmentResponse> findByUserIdAndRolId(
            @PathVariable Long userId,
            @PathVariable Long rolId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findByUserIdAndRolId(UserId.from(userId), RolId.of(rolId), requesterId, requesterRolId)
                .map(readMapper::toRest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/primary")
    public ResponseEntity<ReadAssignmentResponse> findByUserIdAndIsPrimary(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findByUserIdAndIsPrimary(UserId.from(userId), true, requesterId, requesterRolId)
                .map(readMapper::toRest)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping("/permanent")
    public ResponseEntity<ReadAssignmentResponse> savePermanent(
            @Valid @RequestBody CreateAssignmentPermanentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateAssignmentPermanentDto dto = writeMapper.toServicePermanent(request);
        ReadAssignmentDto saved = useCase.savePermanent(dto, requesterId, requesterRolId);
        return ResponseEntity.status(HttpStatus.CREATED).body(readMapper.toRest(saved));
    }

    @PostMapping("/temporary")
    public ResponseEntity<ReadAssignmentResponse> saveTemporary(
            @Valid @RequestBody CreateAssignmentTemporaryRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
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

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        boolean result = useCase.isActiveAt(UserRolAssignmentId.of(id), date, requesterId, requesterRolId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/active")
    public ResponseEntity<Boolean> isCurrentlyActive(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
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

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.extend(UserRolAssignmentId.of(id), newValidTo, requesterId, requesterRolId);
    }





    @DeleteMapping("/user/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeAllRoles(
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.revokeAllRol(UserId.from(userId), requesterId, requesterRolId);
    }

    @DeleteMapping("/user/{userId}/roles/{rolId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeRole(
            @PathVariable Long userId,
            @PathVariable Long rolId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.revokeRol(UserId.from(userId), RolId.of(rolId), requesterId, requesterRolId);
    }
}

