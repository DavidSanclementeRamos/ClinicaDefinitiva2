package com.example.ClinicaDefinitiva.infrastructure.rest.prueva;

import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final UpdatePatientUseCase updatePatientUseCase;

    public PatientController(UpdatePatientUseCase updatePatientUseCase) {
        this.updatePatientUseCase = updatePatientUseCase;
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<Void> updatePatient(
            @PathVariable Long patientId,
            @RequestBody UpdatePatientRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Obtener rol primario del usuario
        Rol primaryRole = userDetails.getPrimaryRole();
        UserId userId = userDetails.getUserId();

        // Ejecutar use case
        UpdatePatientCommand command = new UpdatePatientCommand(
                patientId,
                request.person(),
                request.userIdentity()
        );

        updatePatientUseCase.execute(command, primaryRole, userId);

        return ResponseEntity.ok().build();
    }
}
