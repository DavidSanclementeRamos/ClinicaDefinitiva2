package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Optional<Appointment> findById(Long id);
    List<Appointment> findAll();
    void save(Appointment appointment);
    Appointment Update(Long id, Appointment appointment);
    void deleteById(Long id);


}
