package com.example.ClinicaDefinitiva.infrastructure.security.config;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private static ReceptionRepository receptionistRepository;

    // Inyección del repositorio (puedes hacerlo con @Autowired en Spring)
    public static void setReceptionistRepository(ReceptionRepository repository) {
        receptionistRepository = repository;
    }

    private SecurityUtils() {}

    public static UserIdentityId getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getId();
    }

    public static RolId getCurrentUserRolId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return userDetails.getActiveRolId(); // rol activo
    }

    public static String getCurrentUserSector() {
        UserIdentityId userIdentityId = getCurrentUserId();

        Receptionist receptionist = receptionistRepository.findByUserId(userIdentityId).orElseThrow();       //  .orElseThrow(() -> new IllegalStateException("Receptionist not found for user: " + userId));

        return receptionist.getSector().toString();
    }
}






