package com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.controller;

import com.example.ClinicaDefinitiva.application.administration.operations.dto.AssignShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.CanAccommodateAppointmentDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ExcludedBlockDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.PageShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.ReadShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.dto.RescheduleShiftDto;
import com.example.ClinicaDefinitiva.application.administration.operations.input.ShiftUseCase;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.mapper.ShiftRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.mapper.ShiftRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.AssignShiftRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.ExcludedBlockRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.PageShiftResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.ReadShiftResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.administration.operations.dto.RescheduleShiftRequest;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shifts")
public class ShiftController {

    private final ShiftUseCase useCase;
    private final ShiftRestReadMapper readMapper;
    private final ShiftRestWriteMapper writeMapper;

    public ShiftController(ShiftUseCase useCase, 
                          ShiftRestReadMapper readMapper, 
                          ShiftRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadShiftResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadShiftDto dto = useCase.findById(ShiftId.from(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PageShiftResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageShiftDto> dtos = useCase.findAll(requesterId, requesterRolId);
        return ResponseEntity.ok(dtos.map(readMapper::toPageRest));
    }

    @PostMapping
    public ResponseEntity<ReadShiftResponse> assignShift(
            @Valid @RequestBody AssignShiftRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        AssignShiftDto dto = writeMapper.toService(request);
        ReadShiftDto created = useCase.assignShift(dto, requesterId, requesterRolId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PostMapping("/{id}/exclude-block")
    public ResponseEntity<ReadShiftResponse> excludeBlock(
            @PathVariable Long id,
            @Valid @RequestBody ExcludedBlockRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ExcludedBlockDto dto = writeMapper.toExcludedBlockDto(request);
        ReadShiftDto updated = useCase.excludeBlock(
                ShiftId.from(id), 
                dto, 
                requesterId, 
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/reschedule")
    public ResponseEntity<ReadShiftResponse> reschedule(
            @PathVariable Long id,
            @Valid @RequestBody RescheduleShiftRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        RescheduleShiftDto dto = writeMapper.toRescheduleDto(request);
        ReadShiftDto updated = useCase.reschedule(
                ShiftId.from(id), 
                dto, 
                requesterId, 
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReadShiftResponse> cancel(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadShiftDto updated = useCase.cancel(
                ShiftId.from(id), 
                reason, 
                requesterId, 
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ReadShiftResponse> complete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadShiftDto updated = useCase.complete(
                ShiftId.from(id), 
                requesterId, 
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @GetMapping("/{id}/can-accommodate")
    public ResponseEntity<Boolean> canAccommodateAppointment(
            @PathVariable Long id,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CanAccommodateAppointmentDto dto = 
                writeMapper.toCanAccommodateDto(start, end);

        boolean result = useCase.canAccommodateAppointment(
                ShiftId.from(id), 
                dto, 
                requesterId, 
                requesterRolId
        );

        return ResponseEntity.ok(result);
    }
}
