package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.application.portsInput.actor.DentistUseCase;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentist.DentistRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentist.DentistRestWriteMapper;
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

import java.time.LocalDateTime;

@RestController
@Validated
@RequestMapping("/api/v1/dentists")
public class DentistController {

    private final DentistUseCase useCase;
    private final DentistRestReadMapper readMapper;
    private final DentistRestWriteMapper writeMapper;

    public DentistController(DentistUseCase useCase,
                             DentistRestReadMapper readMapper,
                             DentistRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar dentista por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReadDentistResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadDentistDto dto = useCase.findById(DentistId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    /**
     * Listar todos los dentistas con paginación
     */
    @GetMapping
    public ResponseEntity<Page<PageDentistResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageDentistDto> dentists = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageDentistResponse> response = dentists.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar dentistas por disponibilidad
     */
    @GetMapping("/availability/{status}")
    public ResponseEntity<Page<PageDentistResponse>> findByAvailability(
            @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageDentistDto> dentists = useCase.findByAvailability(
                status,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageDentistResponse> response = dentists.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar dentistas por especialidad
     */
    @GetMapping("/specialty/{specialty}")
    public ResponseEntity<Page<PageDentistResponse>> findBySpecialty(
            @PathVariable String specialty,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageDentistDto> dentists = useCase.findBySpecialty(
                specialty,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageDentistResponse> response = dentists.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Crear un nuevo dentista
     */
    @PostMapping
    public ResponseEntity<ReadDentistResponse> create(
            @Valid @RequestBody CreateDentistRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateDentistDto dto = writeMapper.toServiceCreate(request);
        ReadDentistDto created = useCase.save(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    /**
     * Actualizar datos de contacto del dentista
     */
    @PatchMapping("/{id}/contact")
    public ResponseEntity<ReadDentistResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDentistContactRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateDentistContactDto dto = writeMapper.toServiceUpdateContact(request);
        ReadDentistDto updated = useCase.updateContactData(dto, id, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Actualizar datos sensibles del dentista (solo RRHH)
     */
    @PatchMapping("/{id}/sensitive")
    public ResponseEntity<ReadDentistResponse> updateSensitive(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDentistSensitiveRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateDentistSensitiveDto dto = writeMapper.toServiceUpdateSensitive(request);
        ReadDentistDto updated = useCase.updateSensitiveData(dto, id, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Aplicar vacaciones (solo el dentista puede aplicarlas para sí mismo)
     */
    @PostMapping("/me/vacation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void applyVacation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.applyVacation(start, end, requesterId, requesterRolId);
    }

    /**
     * Aplicar incapacidad (solo el dentista puede aplicarla para sí mismo)
     */
    @PostMapping("/me/incapacity")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void applyIncapacity(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam String note,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.applyIncapacity(start, end, note, requesterId, requesterRolId);
    }

    /**
     * Volver a disponible (solo el dentista puede hacerlo para sí mismo)
     */
    @PatchMapping("/me/return-available")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnToAvailable(@AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.returnToAvailable(requesterId, requesterRolId);
    }

    /**
     * Eliminar dentista (solo RRHH)
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deleteById(DentistId.of(id), requesterId, requesterRolId);
    }
}