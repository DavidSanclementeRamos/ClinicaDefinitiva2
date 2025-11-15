package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.application.dto.ServiceRenderedDto;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.AppointmentId;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {
    Optional<Appointment> findById(AppointmentId id);
    Page<Appointment> findAll(Pageable pageable);
    void save(Appointment appointment);
    Appointment Update(AppointmentId id, Appointment appointment);
    Appointment deleteById(Appointment appointment);


    boolean findByServiceId(ServiceRenderedDto providedServiceId);

    boolean findByPatientId(String patientId);

    boolean findByDentiestId(String dentistId);
}
