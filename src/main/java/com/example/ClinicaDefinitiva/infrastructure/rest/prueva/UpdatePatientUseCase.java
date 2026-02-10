package com.example.ClinicaDefinitiva.infrastructure.rest.prueva;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.service.AuthorizationService;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.Permission;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.SecurityContext;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdatePatientUseCase {

    private final PatientRepository patientRepository;
    private final AuthorizationService authorizationService;

    public UpdatePatientUseCase(PatientRepository patientRepository,
                                AuthorizationService authorizationService) {
        this.patientRepository = patientRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    @RequiresPermission(resource = Resources.PATIENT, action = Actions.UPDATE)
    public void execute(UpdatePatientCommand command, Rol userRol, UserIdentityId userIdentityId) {
        // 1. Buscar paciente
        Patient patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        // 2. Crear contexto de seguridad con ownership
        SecurityContext context = SecurityContext
                .builder(Permission.update(ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT)), userIdentityId)
                .withResourceOwnerId(patient.getUser())
                .build();

        // 3. Validar autorización (incluye ownership)
        if (!authorizationService.isAuthorized(userRol, context)) {
            throw new SecurityException("You can only update your own patient data");
        }

        // 4. Ejecutar lógica de negocio
        patient.updatePatientContact(
                command.person(),
                command.userIdentity()
        );

        // 5. Persistir
        patientRepository.save(patient);
    }
}
