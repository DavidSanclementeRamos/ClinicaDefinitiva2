package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.controller;

import com.example.ClinicaDefinitiva.application.authentication.dto.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.input.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.CreateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.PageUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.ReadUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.UpdateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.mapper.UserIdentityRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.mapper.UserIdentityRestWriteMapper;
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
@RequestMapping("/api/v1/users")
public class UserIdentityController {

    private final UserIdentityUseCase useCase;
    private final UserIdentityRestReadMapper readMapper;
    private final UserIdentityRestWriteMapper writeMapper;

    public UserIdentityController(UserIdentityUseCase useCase,
                                  UserIdentityRestReadMapper readMapper,
                                  UserIdentityRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar usuario por ID
     */
 @GetMapping("/{id}")
public ResponseEntity<ReadUserIdentityResponse> findById(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

    UserIdentityId requesterId = userDetails.getId();
    RolId requesterRolId = userDetails.getActiveRolId();

    ReadUserIdentityDto dto = useCase.findById(UserIdentityId.from(id), requesterId, requesterRolId);
    return ResponseEntity.ok(readMapper.toRestRead(dto));
}



    /**
     * Buscar usuario por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<PageUserIdentityResponse> findByEmail(
            @PathVariable String email,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findByEmail(email, requesterId, requesterRolId)
                .map(readMapper::toRestPage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   



@GetMapping
public ResponseEntity<Page<PageUserIdentityResponse>> findAll(
        @PageableDefault(size = 20) Pageable pageable,
        @RequestParam(required = false) String status,
        @AuthenticationPrincipal CustomUserDetails userDetails) {

    UserIdentityId requesterId = userDetails.getId();
    RolId requesterRolId = userDetails.getActiveRolId();

    Page<PageUserIdentityDto> users;
    if (status != null && !status.isBlank()) {
        users = useCase.findAllByStatus(status, pageable, requesterId, requesterRolId);
    } else {
        users = useCase.findAll(pageable, requesterId, requesterRolId);
    }

    Page<PageUserIdentityResponse> response = users.map(readMapper::toRestPage);
    return ResponseEntity.ok(response);
}
 

    /**
     * Registrar un nuevo usuario
     */
    @PostMapping("/register")
    public ResponseEntity<ReadUserIdentityResponse> register(
            @Valid @RequestBody CreateUserIdentityRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateUserIdentityDto dto = writeMapper.toServiceCreate(request);
        ReadUserIdentityDto registered = useCase.register(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRestRead(registered));
    }

    /**
     * Actualizar un usuario
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReadUserIdentityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserIdentityRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateUserIdentityDto dto = writeMapper.toServiceUpdate(request);
        ReadUserIdentityDto updated = useCase.update(dto, id, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRestRead(updated));
    }

    /**
     * Desactivar un usuario
     */
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ReadUserIdentityResponse> deactivate(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadUserIdentityDto deactivated = useCase.deactivate(
                UserIdentityId.from(id),
                reason != null ? reason : "No reason provided",
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRestRead(deactivated));
    }

    /**
     * Suspender un usuario
     */
    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ReadUserIdentityResponse> suspend(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadUserIdentityDto suspended = useCase.suspend(
                UserIdentityId.from(id),
                reason != null ? reason : "No reason provided",
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRestRead(suspended));
    }

    /**
     * Reactivar un usuario
     */
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ReadUserIdentityResponse> reactivate(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadUserIdentityDto reactivated = useCase.reactivate(
                UserIdentityId.from(id),
                reason != null ? reason : "No reason provided",
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRestRead(reactivated));
    }

   

    
}


