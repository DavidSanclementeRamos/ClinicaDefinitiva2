package com.example.ClinicaDefinitiva.domain.administration.accessControl;

import java.time.LocalDate;

public class UserRolAssignment {
    ///  Entidad assignation de roles a usuarios
    long id;
    long userId;
    long rolesId;
    LocalDate validFrom;
    LocalDate validTo;
    boolean primary;
}
