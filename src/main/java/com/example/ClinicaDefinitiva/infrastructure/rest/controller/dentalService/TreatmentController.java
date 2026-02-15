package com.example.ClinicaDefinitiva.infrastructure.rest.controller.dentalService;

import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.CreateTreatmentDto;
import com.example.ClinicaDefinitiva.application.dto.dentalService.treatment.TreatmentDto;
import com.example.ClinicaDefinitiva.application.portsInput.dentalService.TreatmentUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dental.care.service.num.TreatmentStatus;
import com.example.ClinicaDefinitiva.domain.dental.care.service.vo.TreatmentId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.CreateTreatmentRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.ReadTreatmentResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService.TreatmentRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService.TreatmentRestWriteMapper;
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


@RestController
@Validated
@RequestMapping("/api/v1/treatments")
public class TreatmentController {

    private final TreatmentUseCase useCase;
    private final TreatmentRestReadMapper readMapper;
    private final TreatmentRestWriteMapper writeMapper;

    public TreatmentController(TreatmentUseCase useCase,
                               TreatmentRestReadMapper readMapper,
                               TreatmentRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReadTreatmentResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        TreatmentDto dto = useCase.findById(TreatmentId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }


    @GetMapping
    public ResponseEntity<Page<ReadTreatmentResponse>> findAll(
            @PageableDefault(size = 20, sort = "startDate,desc") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<TreatmentDto> treatments = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<ReadTreatmentResponse> response = treatments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<Page<ReadTreatmentResponse>> findByStatus(
            @PathVariable TreatmentStatus status,
            @PageableDefault(size = 20, sort = "startDate,desc") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<TreatmentDto> treatments = useCase.findByStatus(
                status,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<ReadTreatmentResponse> response = treatments.map(readMapper::toRest);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ReadTreatmentResponse> create(
            @Valid @RequestBody CreateTreatmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateTreatmentDto dto = writeMapper.toServiceCreate(request);
        TreatmentDto created = useCase.create(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ReadTreatmentResponse> complete(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualEndDate,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        TreatmentDto completed = useCase.complete(
                TreatmentId.of(id),
                actualEndDate,
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(completed));
    }


    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReadTreatmentResponse> cancel(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        TreatmentDto cancelled = useCase.cancel(
                TreatmentId.of(id),
                reason,
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(cancelled));
    }
}
