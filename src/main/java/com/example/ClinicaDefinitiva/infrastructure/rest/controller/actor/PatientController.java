package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.*;
import com.example.ClinicaDefinitiva.application.portsInput.actor.PatientUseCase;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper.PatientRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper.PatientRestWriteMapper;
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
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientUseCase useCase;
    private final PatientRestReadMapper readMapper;
    private final PatientRestWriteMapper writeMapper;

    public PatientController(PatientUseCase useCase,
                             PatientRestReadMapper readMapper,
                             PatientRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar patient por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReadPatientResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadPatientDto dto = useCase.findById(PatientId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    /**
     * Listar todos los patients con paginación
     */
    @GetMapping
    public ResponseEntity<Page<PagePatientResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PagePatientDto> patients = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PagePatientResponse> response = patients.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar patients por ID de contrato
     */
    @GetMapping("/contract/{contractId}")
    public ResponseEntity<Page<PagePatientResponse>> findByContractId(
            @PathVariable Long contractId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PagePatientDto> patients = useCase.findByContractId(
                ContractId.fromLong(contractId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PagePatientResponse> response = patients.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar patients por ID de guardian
     */
    @GetMapping("/guardian/{guardianId}")
    public ResponseEntity<Page<PagePatientResponse>> findByGuardianId(
            @PathVariable Long guardianId,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PagePatientDto> patients = useCase.findByGuardianId(
                GuardianId.fromLong(guardianId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PagePatientResponse> response = patients.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Crear un nuevo patient
     */
    @PostMapping
    public ResponseEntity<ReadPatientResponse> create(
            @Valid @RequestBody CreatePatientRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreatePatientDto dto = writeMapper.toServiceCreate(request);
        ReadPatientDto created = useCase.save(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    /**
     * Actualizar datos de contacto del patient
     */
    @PatchMapping("/{id}/contact")
    public ResponseEntity<ReadPatientResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientContactRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdatePatientContactDto dto = writeMapper.toServiceUpdateContact(request);
        ReadPatientDto updated = useCase.updateContactData(dto, PatientId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Actualizar datos sensibles del patient (solo RECEPTIONIST)
     */
    @PatchMapping("/{id}/sensitive")
    public ResponseEntity<ReadPatientResponse> updateSensitive(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientSensitiveRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdatePatientSensitiveDto dto = writeMapper.toServiceUpdateSensitive(request);
        ReadPatientDto updated = useCase.updateSensitiveData(dto, PatientId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Eliminar patient (solo RECEPTIONIST)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deleteById(PatientId.of(id), requesterId, requesterRolId);
    }
}
