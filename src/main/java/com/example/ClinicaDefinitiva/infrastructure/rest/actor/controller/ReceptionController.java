
package com.example.ClinicaDefinitiva.infrastructure.rest.actor.controller;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.CreateReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistContactDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.UpdateReceptionistSensitiveDto;
import com.example.ClinicaDefinitiva.application.actor.portsInput.ReceptionUseCase;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.CreateReceptionistRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.PageReceptionistResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.ReadReceptionistResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.UpdateReceptionistContactRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.UpdateReceptionistSensitiveRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.reception.ReceptionistRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.reception.ReceptionistRestWriteMapper;
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
@RequestMapping("/api/v1/receptionists")
public class ReceptionController {

    private final ReceptionUseCase useCase;
    private final ReceptionistRestReadMapper readMapper;
    private final ReceptionistRestWriteMapper writeMapper;

    public ReceptionController(ReceptionUseCase useCase,
                               ReceptionistRestReadMapper readMapper,
                               ReceptionistRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar receptionist por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReadReceptionistResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadReceptionistDto dto = useCase.findById(ReceptionId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    /**
     * Listar todos los receptionists con paginación
     */
    @GetMapping
    public ResponseEntity<Page<PageReceptionistResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageReceptionistDto> receptionists = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageReceptionistResponse> response = receptionists.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Buscar receptionists por sector
     */
    @GetMapping("/sector/{sector}")
    public ResponseEntity<Page<PageReceptionistResponse>> findBySector(
            @PathVariable String sector,
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageReceptionistDto> receptionists = useCase.findBySector(
                sector,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageReceptionistResponse> response = receptionists.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    /**
     * Crear un nuevo receptionist
     */
    @PostMapping
    public ResponseEntity<ReadReceptionistResponse> create(
            @Valid @RequestBody CreateReceptionistRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateReceptionistDto dto = writeMapper.toServiceCreate(request);
        ReadReceptionistDto created = useCase.save(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    /**
     * Actualizar datos de contacto del receptionist
     */
    @PatchMapping("/{id}/contact")
    public ResponseEntity<ReadReceptionistResponse> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReceptionistContactRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateReceptionistContactDto dto = writeMapper.toServiceUpdateContact(request);
        ReadReceptionistDto updated = useCase.updateContact(dto, ReceptionId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Actualizar datos sensibles del receptionist
     */
    @PatchMapping("/{id}/sensitive")
    public ResponseEntity<ReadReceptionistResponse> updateSensitive(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReceptionistSensitiveRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateReceptionistSensitiveDto dto = writeMapper.toServiceUpdateSensitive(request);
        ReadReceptionistDto updated = useCase.updateSensitive(dto, ReceptionId.of(id), requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    /**
     * Eliminar receptionist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deleteById(ReceptionId.of(id), requesterId, requesterRolId);
    }
}