package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ServiceDeactivationValidator {
    private final AppointmentRepository repository;

    public ServiceDeactivationValidator(AppointmentRepository repository) {
        this.repository = repository;
    }

    public void validate(ServiceId serviceId) {
                Pageable defaultPageable = PageRequest.of(0, 20, Sort.by("id").ascending());
        Page<Appointment> schedule = repository.findByServiceId(serviceId, defaultPageable);

        if (!schedule.isEmpty()) {
            throw BusinessRuleViolationException(  
                    ProvidedServiceError.ERR_SERVICE_HAS_APPOINTMENTS,
                    
                    EntityContext.DENTAL_SERVICE
            );
        }

    }
}
