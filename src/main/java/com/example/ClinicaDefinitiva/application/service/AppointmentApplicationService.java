package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.application.mapper.AppointmentMapper;
import com.example.ClinicaDefinitiva.application.usecase.AppointmentUseCase;
import com.example.ClinicaDefinitiva.domain.portsInput.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.service.AppointmentDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class AppointmentApplicationService implements AppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentDomainService schedulingDomainService;
    private final AppointmentMapper mapper;

    public AppointmentApplicationService(AppointmentRepository appointmentRepository, AppointmentDomainService schedulingDomainService, AppointmentMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.schedulingDomainService = schedulingDomainService;
        this.mapper = mapper;
    }

    @Override
    public Appointment save(Long dentistId, Long patientId, Long providedServiceId, LocalDateTime start, LocalDateTime end) {
        // load dentist/patient/schedule via repos in real app (omitted)
        Appointment a = schedulingDomainService.schedule(dentistId, patientId, start, end);
        appointmentRepository.save(a);
        return a;
    }
    @Override
    public Appointment update(Long appointmentId, LocalDateTime newStart, LocalDateTime newEnd) {
        Appointment original = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        Appointment updated = schedulingDomainService.reschedule(original, newStart, newEnd);
        appointmentRepository.save(updated);
        return updated;
    }

    @Override
    public void daleById(Long appointmentId) {
        Appointment a = appointmentRepository.findById(appointmentId).orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
        a.cancel();
        appointmentRepository.save(a);
    }
}





