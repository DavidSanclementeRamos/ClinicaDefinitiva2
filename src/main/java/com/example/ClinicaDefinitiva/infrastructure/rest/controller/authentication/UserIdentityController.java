package com.example.ClinicaDefinitiva.infrastructure.rest.controller.authentication;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.portsInput.authentication.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.CreateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.PageUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.ReadUserIdentityResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UpdateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity.UserIdentityRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity.UserIdentityRestWriteMapper;
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

        return useCase.findById(UserIdentityId.from(id), requesterId, requesterRolId)
                .map(readMapper::toRestRead)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Listar todos los usuarios con paginación
     */
    @GetMapping
    public ResponseEntity<Page<PageUserIdentityResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageUserIdentityDto> users = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageUserIdentityResponse> response = users.map(readMapper::toRestPage);

        return ResponseEntity.ok(response);
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

    /**
     * Buscar usuario por email y estado
     */
    @GetMapping("/email/{email}/status/{status}")
    public ResponseEntity<PageUserIdentityResponse> findByEmailAndStatus(
            @PathVariable String email,
            @PathVariable String status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findByEmailAndStatus(email, status, requesterId, requesterRolId)
                .map(readMapper::toRestPage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Buscar usuario por ID y estado
     */
    @GetMapping("/{id}/status/{status}")
    public ResponseEntity<PageUserIdentityResponse> findByIdAndStatus(
            @PathVariable Long id,
            @PathVariable String status,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        return useCase.findByIdAndStatus(UserIdentityId.from(id), status, requesterId, requesterRolId)
                .map(readMapper::toRestPage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    /**
     * Autenticar un usuario
     */
   /** @PostMapping("/authenticate")
    public ResponseEntity<ReadUserIdentityResponse> authenticate(
            @Valid @RequestBody AuthenticateUserRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadUserIdentityDto authenticated = useCase.authenticate(
                request.getEmail(),
                request.getPassword(),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRestRead(authenticated));
    }*/
}


