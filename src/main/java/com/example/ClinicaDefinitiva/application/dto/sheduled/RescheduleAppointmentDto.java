package com.example.ClinicaDefinitiva.application.dto.sheduled;

import java.time.LocalDateTime;

public class RescheduleAppointmentDto {
    public final String appointmentId;
    public final String dentistId;
    public final String patientId;
    public final LocalDateTime newStart;
    public final LocalDateTime newEnd;
    public RescheduleAppointmentDto(String appointmentId, String dentistId, String patientId, LocalDateTime newStart, LocalDateTime newEnd) {
        this.appointmentId = appointmentId; this.dentistId = dentistId; this.patientId = patientId; this.newStart = newStart; this.newEnd = newEnd;
    }


}
