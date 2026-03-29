package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.administration.authorization.UserRolAssignmentError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;

import java.time.LocalDate;

/**
 * Representa la asignación de un rol a un usuario
 * Soporta roles temporales y múltiples roles simultáneos
 */
public class UserRolAssignment {
    private UserRolAssignmentId id;
    private final UserIdentityId userIdentityId;
    private final RolId rolId;
    private final LocalDate validFrom;
    private LocalDate validTo;
    private final boolean isPrimary; // Rol principal para UI

    private UserRolAssignment(UserRolAssignmentId id, UserIdentityId userIdentityId, RolId rolId,
                              LocalDate validFrom, LocalDate validTo, boolean isPrimary) {
        validateDates(validFrom, validTo);
        this.id = id;
        this.userIdentityId = userIdentityId;
        this.rolId = rolId;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.isPrimary = isPrimary;
    }


    public static UserRolAssignment assignPermanent(UserIdentityId userIdentityId, RolId rolId, boolean isPrimary) {
        return new UserRolAssignment(
                null,
                userIdentityId,
                rolId,
                LocalDate.now(),
                null, // Sin fecha de expiración
                isPrimary
        );
    }


    public static UserRolAssignment assignTemporary(UserIdentityId userIdentityId, RolId rolId,
                                                    LocalDate validFrom, LocalDate validTo,
                                                    boolean isPrimary) {
        if (isPrimary) {
            throw new BusinessRuleViolationException(
                    UserRolAssignmentError.ERR_ASSIGNMENT_TEMPORARY_CANNOT_BE_PRIMARY,
                    EntityContext.ASSIGNMENT
            );
        }


        return new UserRolAssignment(
                null,
                userIdentityId,
                rolId,
                validFrom,
                validTo,
                false
        );
    }



    public boolean isActiveAt(LocalDate date) {
        if (validFrom != null && date.isBefore(validFrom)) {
            return false;
        }
        return validTo == null || !date.isAfter(validTo);
    }


    public boolean isCurrentlyActive() {
        return isActiveAt(LocalDate.now());
    }


    public void extend(LocalDate newEndDate) {
        if (validTo == null) {
            throw new DomainAggregateException(UserRolAssignmentError.ERR_ASSIGNMENT_CANNOT_EXTEND_PERMANENT, EntityContext.ASSIGNMENT);
        }
        if (newEndDate.isBefore(validTo)) {
            throw new DomainAggregateException(UserRolAssignmentError.ERR_ASSIGNMENT_INVALID_EXTENSION_DATE, EntityContext.ASSIGNMENT);
        }
        this.validTo = newEndDate;
    }


    public void revoke() {
        this.validTo = LocalDate.now().minusDays(1);
    }

    private void validateDates(LocalDate validFrom, LocalDate validTo) {
        if (validFrom == null) {
            throw new DomainAggregateException(UserRolAssignmentError.ERR_ASSIGNMENT_VALID_FROM_REQUIRED, EntityContext.ASSIGNMENT);
        }
        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new DomainAggregateException(UserRolAssignmentError.ERR_ASSIGNMENT_INVALID_DATE_RANGE,EntityContext.ASSIGNMENT);
        }
    }

    public UserRolAssignmentId getId() { return id; }
    public UserIdentityId getUserId() { return userIdentityId; }
    public RolId getRolId() { return rolId; }
    public LocalDate getValidFrom() { return validFrom; }
    public LocalDate getValidTo() { return validTo; }
    public boolean isPrimary() { return isPrimary; }
    public void setId(UserRolAssignmentId id) { this.id = id; }
    
    // Agregar en UserRolAssignment.java
public static UserRolAssignment reconstruct(
        UserRolAssignmentId id,
        UserIdentityId userIdentityId,
        RolId rolId,
        LocalDate validFrom,
        LocalDate validTo,
        boolean isPrimary) {
    
    UserRolAssignment assignment = new UserRolAssignment(
        id, userIdentityId, rolId, validFrom, validTo, isPrimary);
    return assignment;
}
}


