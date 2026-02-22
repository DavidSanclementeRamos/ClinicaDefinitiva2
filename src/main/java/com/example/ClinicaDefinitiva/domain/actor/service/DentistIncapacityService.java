package com.example.ClinicaDefinitiva.domain.actor.service;

import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.output.AppointmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class DentistIncapacityService {

    private final AppointmentRepository appointmentRepository;
    private final DentistRepository dentistRepository;

    public DentistIncapacityService(AppointmentRepository appointmentRepository,
                                     DentistRepository dentistRepository) {
        this.appointmentRepository = appointmentRepository;
        this.dentistRepository = dentistRepository;
    }

    public void registerIncapacity(DentistId dentistId, LocalDateTime start, LocalDateTime end,String medicalNote) {

        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new DentistNotFoundException(""));

        dentist.applyIncapacity(start, end, medicalNote);
        dentistRepository.save(dentist);

        Page<Appointment> conflicts = appointmentRepository.findByDentistBetween(dentistId, start, end, Pageable.unpaged());

        for (Appointment appointment : conflicts) {
            appointment.cancel(medicalNote);
        }
    }
}

