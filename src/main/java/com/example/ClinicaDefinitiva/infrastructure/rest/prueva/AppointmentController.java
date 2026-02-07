package com.example.ClinicaDefinitiva.infrastructure.rest.prueva;

import com.example.ClinicaDefinitiva.application.portsInput.AppointmentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final AppointmentUseCase appointmentUseCase;
    private final AppointmentReadMapper readMapper;

    public AppointmentController(AppointmentUseCase appointmentUseCase,
                                 AppointmentReadMapper readMapper) {
        this.appointmentUseCase = appointmentUseCase;
        this.readMapper = readMapper;
    }

    @PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_APPOINTMENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
