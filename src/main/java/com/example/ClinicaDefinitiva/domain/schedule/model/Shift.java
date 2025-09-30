package com.example.ClinicaDefinitiva.domain.schedule.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Shift {
// turno

    private LocalDateTime start;
    private LocalDateTime end;
    private List<Appointment> appointments;

    // No puede modificarse el horario si ya existen citas en ese rango
    public void reschedule(LocalDateTime newStart, LocalDateTime newEnd) {
  List<Appointment> conflicts = appointments.stream()
                .filter(a ->
                        a.getDateTime().isAfter(newStart.minusSeconds(1)) &&
                                a.getDateTime().isBefore(newEnd.plusSeconds(1))
                )
                .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new BusinessRuleException(
                    "Cannot reschedule: " + conflicts.size() + " appointment"
                            + (conflicts.size() > 1 ? "s" : "") + " scheduled in the new range"
            );
        }

        this.start = newStart;
        this.end = newEnd;

    }

    }
