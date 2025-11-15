package com.example.ClinicaDefinitiva.application.usecase;

import com.example.ClinicaDefinitiva.application.dto.sheduled.ReadAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.UpdateAppointmentDto;
import com.example.ClinicaDefinitiva.application.dto.sheduled.CreateAppointmentDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentUseCase {
    ReadAppointmentDto findId(Long appointmentId);
    Page<ReadAppointmentDto> findAll(Pageable pageable);
    ReadAppointmentDto save(CreateAppointmentDto dto);
    ReadAppointmentDto update(UpdateAppointmentDto dto);
    ReadAppointmentDto daleById(UpdateAppointmentDto  up);

}
