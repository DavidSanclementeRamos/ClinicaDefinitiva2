package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.AppointmentNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.DentalServiceNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.actorException.PatientNotFoundException;
import com.example.ClinicaDefinitiva.application.portsInput.AppointmentUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;
import com.example.ClinicaDefinitiva.domain.dental.care.services.vo.ServiceId;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.ProvidedServiceRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.vo.AppointmentId;
import com.example.ClinicaDefinitiva.domain.schedule.service.AppointmentSchedulingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentApplicationService implements AppointmentUseCase {

    private final AppointmentRepository appointmentRepository;
    private final ProvidedServiceRepository serviceRepository;
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final AppointmentMapper mapper;

    public AppointmentApplicationService(AppointmentRepository appointmentRepository, ProvidedServiceRepository serviceRepository, PatientRepository patientRepository, DentistRepository dentistRepository, AppointmentMapper mapper) {
        this.appointmentRepository = appointmentRepository;
        this.serviceRepository = serviceRepository;
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.mapper = mapper;
    }


    @Override
    public ReadAppointmentDto findId(Long appointmentId) {
        // convertir Long -> InvoiceId VO y buscar
        AppointmentId id = AppointmentId.fromString(String.valueOf(appointmentId));
       Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Appointment not encontrado"));
        return mapper.toAppointmentDto(appointment);
    }

    @Override
    public Page<ReadAppointmentDto> findAll(Pageable pageable) {
       Page<Appointment> appointments = (Page<Appointment>) appointmentRepository.findAll(pageable);
       if(appointments.isEmpty() ){
           throw new IllegalArgumentException("No found");
       }
        return appointments.map(mapper::toAppointmentDto);
    }

    @Override
    public ReadAppointmentDto save(CreateAppointmentDto dto) {

        // conversion de String a PatientId
        PatientId paId = PatientId.fromString(dto.patientId);
        Patient patient = patientRepository.findById(paId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado: " + paId));

        // conversion de String a DentistId
        DentistId deId = DentistId.fromString(dto.dentistId);
        Dentist dentist = dentistRepository.findById(deId)
                .orElseThrow(() -> new DentistNotFoundException("Odontólogo no encontrado: " + deId));

        // conversion de String a PatientId
        ServiceId seId = ServiceId.fromString(dto.providedServiceId);
        ProvidedService service = serviceRepository.findById(seId)
                .orElseThrow(() -> new DentalServiceNotFoundException("Servicio no encontrado: " + seId));

        Appointment appointment =  AppointmentSchedulingService.registerSchedule(
                dentist,
                patient,
                dto.start,
                dto.end,
                dto.type,
                dto.reason,
                dto.serviceDuration,
                service

        );
                appointmentRepository.save(appointment);
        return mapper.toAppointmentDto(appointment);
    }

    @Override
    public ReadAppointmentDto update(UpdateAppointmentDto dto) {
        // conversion de String a PatientId
        PatientId paId = PatientId.fromString(dto.getPatientId());
        Patient patient = patientRepository.findById(paId)
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado: " + paId));

        // conversion de String a DentistId
        DentistId deId = DentistId.fromString(dto.dentistId);
        Dentist dentist = dentistRepository.findById(deId)
                .orElseThrow(() -> new DentistNotFoundException("Odontólogo no encontrado: " + deId));

        // conversion de String a PatientId
        ServiceId seId = ServiceId.fromString(dto.providedServiceId);
        ProvidedService service = serviceRepository.findById(seId)
                .orElseThrow(() -> new DentalServiceNotFoundException("Servicio no encontrado: " + seId));

        // conversion de String a Appointment
        AppointmentId apId = AppointmentId.fromString(dto.appointmentId);
        Appointment appointment = appointmentRepository.findById(apId)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada: " + apId));


        // Pasamos la entidad concreta al DomainService
        appointment = AppointmentSchedulingService.validationReschedule(
                appointment,
                dto.getNewStart(),
                dto.getNewEnd(),
                dto.getSchedule(),
                patient,
                dentist
                );
                appointmentRepository.save(appointment);

        return mapper.toAppointmentDto(appointment);
    }

    @Override
    public ReadAppointmentDto daleById(UpdateAppointmentDto appointmentId) {

        // conversion de String a Appointment
        AppointmentId apId = AppointmentId.fromString(appointmentId.appointmentId);
        Appointment app =  appointmentRepository.findById(apId).
                orElseThrow(()  -> new AppointmentNotFoundException("Cita no encontrada: " + apId));

        Appointment appointment = AppointmentSchedulingService.cancelarCita(app);
        appointmentRepository.save(appointment);
        return mapper.toAppointmentDto(appointment);

    }
}





