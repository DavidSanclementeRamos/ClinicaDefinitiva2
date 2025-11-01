package com.example.ClinicaDefinitiva.infrastructure.repository;

import java.time.LocalDateTime;

public interface CalendarService {
    boolean isSlotAvailable(Long dentistId, LocalDateTime start, LocalDateTime end);
    boolean tryReserveSlot(Long dentistId, LocalDateTime start, LocalDateTime end, String correlationId);
    void confirmReservation(Long dentistId, LocalDateTime start, LocalDateTime end, String correlationId);
    void releaseSlot(Long dentistId, LocalDateTime start, LocalDateTime end, String correlationId);


}
