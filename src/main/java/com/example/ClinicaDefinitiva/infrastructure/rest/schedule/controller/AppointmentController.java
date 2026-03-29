package com.example.ClinicaDefinitiva.infrastructure.rest.schedule.controller;

import com.example.ClinicaDefinitiva.application.schedule.dto.AppointmentCompletionDTO;
import com.example.ClinicaDefinitiva.application.schedule.dto.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.dto.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.dto.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.schedule.input.AppointmentUseCase;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.AppointmentCompletionRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.CreateAppointmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.ReadAppointmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.dto.UpdateAppointmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.mapper.AppointmentRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.schedule.mapper.AppointmentRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@Validated
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentUseCase useCase;
    private final AppointmentRestReadMapper readMapper;
    private final AppointmentRestWriteMapper writeMapper;

    public AppointmentController(AppointmentUseCase useCase,
                                 AppointmentRestReadMapper readMapper,
                                 AppointmentRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    /**
     * Buscar cita por ID
     */
   @GetMapping("/{id}")
    public ResponseEntity<ReadAppointmentResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAppointmentDto dto = useCase.findById(AppointmentId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }

   
    @GetMapping
    public ResponseEntity<Page<ReadAppointmentResponse>> findAll(
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByPatientId(
            @PathVariable Long patientId,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByPatientId(
                PatientId.of(patientId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/date-range")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime end,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByDateRange(
                start,
                end,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }

   
    @GetMapping("/dentist/{dentistId}")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByDentistId(
            @PathVariable Long dentistId,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByDentistId(
                DentistId.of(dentistId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/service/{serviceId}")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByServiceId(
            @PathVariable Long serviceId,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByServiceId(
                ServiceId.of(serviceId),
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByStatus(
            @PathVariable AppointmentStatus status,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByStatus(
                status,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/patient/{patientId}/dentist/{dentistId}")
    public ResponseEntity<Page<ReadAppointmentResponse>> findByPatientAndDentist(
            @PathVariable Long patientId,
            @PathVariable Long dentistId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @PageableDefault(size = 20, sort = "appointmentDate") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<ReadAppointmentDto> appointments = useCase.findByPatientAndDentist(
                PatientId.of(patientId),
                DentistId.of(dentistId),
                start,
                end,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadAppointmentResponse> response = appointments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ReadAppointmentResponse> create(
            @Valid @RequestBody CreateAppointmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateAppointmentDto dto = writeMapper.toServiceCreate(request);
        ReadAppointmentDto created = useCase.save(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<ReadAppointmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateAppointmentDto dto = writeMapper.toServiceUpdate(request);
        ReadAppointmentDto updated = useCase.update(dto, requesterId, requesterRolId);

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReadAppointmentResponse> cancel(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAppointmentDto cancelled = useCase.cancel(
                AppointmentId.of(id),
                reason != null ? reason : "No reason provided",
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(cancelled));
    }

    
   /** @PatchMapping("/{id}/complete")
    public ResponseEntity<ReadAppointmentResponse> complete(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentCompletionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        AppointmentCompletionDTO completionDTO = writeMapper.toServiceCompletion(request);
        ReadAppointmentDto completed = useCase.complete(
                AppointmentId.of(id),
                completionDTO,
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(completed));
    }*/

    
    @PatchMapping("/{id}/no-show")
    public ResponseEntity<ReadAppointmentResponse> markAsNoShow(
            @PathVariable Long id,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAppointmentDto noShow = useCase.markAsNoShow(
                AppointmentId.of(id),
                reason != null ? reason : "Patient did not show up",
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(noShow));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<ReadAppointmentResponse> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadAppointmentDto deleted = useCase.daleById(
                AppointmentId.of(id),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(deleted));
    }
}


