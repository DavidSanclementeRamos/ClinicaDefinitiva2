package com.example.ClinicaDefinitiva.infrastructure.rest.actor.controller;

import com.example.ClinicaDefinitiva.application.actor.dto.guardian.CreateGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.PageGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.ReadGuardianDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.guardian.UpdateGuardianSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.portsInput.GuardianUseCase;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.CreateGuardianRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.PageGuardianResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.ReadGuardianResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.guardian.UpdateGuardianSensitiveRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.guardian.GuardianRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.guardian.GuardianRestWriteMapper;
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
@RequestMapping("/api/v1/guardians")
public class GuardianController {

    private final GuardianUseCase useCase;
    private final GuardianRestReadMapper readMapper;
    private final GuardianRestWriteMapper writeMapper;

    public GuardianController(GuardianUseCase useCase,
                              GuardianRestReadMapper readMapper,
                              GuardianRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar guardian por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReadGuardianResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadGuardianDto dto = useCase.findById(GuardianId.fromLong(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    /**
     * Listar todos los guardians con paginación
     */
    @GetMapping
    public ResponseEntity<Page<PageGuardianResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageGuardianDto> guardians = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageGuardianResponse> response = guardians.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar guardians por ID de paciente
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<PageGuardianResponse>> findByPatientId(
            @PathVariable Long patientId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageGuardianDto> guardians = useCase.findByPatientId(
                PatientId.of(patientId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageGuardianResponse> response = guardians.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Crear un nuevo guardian
     */
    @PostMapping
    public ResponseEntity<ReadGuardianResponse> create(
            @Valid @RequestBody CreateGuardianRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateGuardianDto dto = writeMapper.toServiceCreate(request);
        ReadGuardianDto created = useCase.save(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    /**
     * Actualizar datos de contacto del guardian
     */
    @PatchMapping("/{id}/contact")
    public ResponseEntity<ReadGuardianResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGuardianContactRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateGuardianContactDto dto = writeMapper.toServiceUpdateContact(request);
        ReadGuardianDto updated = useCase.updateContactData(dto, GuardianId.fromLong(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Actualizar datos sensibles del guardian (solo RECEPTIONIST)
     */
    @PatchMapping("/{id}/sensitive")
    public ResponseEntity<ReadGuardianResponse> updateSensitive(
            @PathVariable Long id,
            @Valid @RequestBody UpdateGuardianSensitiveRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateGuardianSensitiveDto dto = writeMapper.toServiceUpdateSensitive(request);
        ReadGuardianDto updated = useCase.updateSensitiveData(dto, GuardianId.fromLong(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Eliminar guardian (solo RECEPTIONIST)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deleteById(GuardianId.fromLong(id), requesterId, requesterRolId);
    }
}
