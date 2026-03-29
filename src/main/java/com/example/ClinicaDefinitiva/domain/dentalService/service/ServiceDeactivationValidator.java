package com.example.ClinicaDefinitiva.domain.dentalService.service;

import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.dentalService.ProvidedServiceError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.springframework.stereotype.Service;


@Service
public class ServiceDeactivationValidator {

    private final AppointmentRepository repository;
    private final InvoiceRepository invoiceRepository;

    public ServiceDeactivationValidator(AppointmentRepository repository, InvoiceRepository invoiceRepository) {
        this.repository = repository;
        this.invoiceRepository = invoiceRepository;
    }

    

    public void validateNoAppointments(ServiceId serviceId) {

        if (invoiceRepository.existsByServiceId(serviceId)) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_HAS_PENDING_INVOICES,
                    EntityContext.DENTAL_SERVICE
            );
        }
        
        if (repository.existsByServiceId(serviceId)) {
            throw new BusinessRuleViolationException(
                    ProvidedServiceError.ERR_SERVICE_HAS_APPOINTMENTS,
                    EntityContext.DENTAL_SERVICE
            );
        }

    }
    
}
