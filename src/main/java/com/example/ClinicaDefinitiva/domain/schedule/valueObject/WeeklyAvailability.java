package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class WeeklyAvailability {
    // disponibilidad semana
    private final List<TimeSlot> slots;

    public WeeklyAvailability(Collection<TimeSlot> slots) {
        this.slots = slots == null ? List.of() : List.copyOf(slots);
    }

    public int totalHoras() {
        return slots.stream().mapToInt(TimeSlot::duracionHoras).sum();
    }

    public boolean cumpleMinimoHoras(int minimo) {
        return totalHoras() >= minimo;
    }

    public List<TimeSlot> getSlots() {
        return List.copyOf(slots);
    }

    public boolean tieneSolapamientos() {
        List<TimeSlot> list = new ArrayList<>(slots);
        list.sort(Comparator.comparing(TimeSlot::getDayOfWeek)
                .thenComparing(TimeSlot::getInicio));
        for (int i = 0; i < list.size() - 1; i++) {
            if (list.get(i).seSolapaCon(list.get(i + 1))) return true;
        }
        return false;
    }



}
