package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;

public class ServiceDeactivationValidator {

    private final AppointmentRepository repository;

    public ServiceDeactivationValidator(AppointmentRepository repository) {
        this.repository = repository;
    }

    public void validateNoAppointments(ServiceId serviceId) {

        if (repository.existsByServiceId(serviceId)) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_HAS_APPOINTMENTS,
                    EntityContext.DENTAL_SERVICE
            );
        }

    }
}
