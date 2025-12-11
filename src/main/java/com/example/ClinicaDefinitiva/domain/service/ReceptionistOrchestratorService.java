package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.errors.ErrorCatalog;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;

public class ReceptionistOrchestratorService {

    public Receptionist registerReceptionist(ReceptionId id, Person data, UserIdentity user, Sector sector) {
        if (!user.isActive()) {
            throw new BusinessRuleViolationException(
                    ErrorCatalog.ERR_SECRETARY_CREATION_REQUIRES_ACTIVE_USER,
                    ContextoEntidad.RECEPTIONIST
            );
        }
        return Receptionist.registerReceptionist(id, data, user, sector);
    }

    public Receptionist updateContactData(Receptionist receptionist, Address address, PhoneNumber phoneNumber, UserIdentity user) {
        if (!user.isActive()) {
            throw new BusinessRuleViolationException(
                    ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE,
                    ContextoEntidad.RECEPTIONIST
            );
        }
        receptionist.updateContactData(address, phoneNumber, user);
        return receptionist;
    }

    public Receptionist updateSensitiveData(Receptionist receptionist, Age age, BloodType bloodType, DateOfBirth dob,
                                            String dni, String documentoEPS, FullName fullname, Sector sector, UserIdentity user) {
        if (!user.isActive()) {
            throw new BusinessRuleViolationException(
                    ErrorCatalog.ERR_RECEPTIONIST_NOT_EDITABLE,
                    ContextoEntidad.RECEPTIONIST
            );
        }
        receptionist.updateSensitiveData(age, bloodType, dob, dni, documentoEPS, fullname, sector, user);
        return receptionist;
    }
}