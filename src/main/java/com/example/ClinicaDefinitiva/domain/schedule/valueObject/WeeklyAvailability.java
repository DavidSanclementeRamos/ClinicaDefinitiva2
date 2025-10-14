package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public final class WeeklyAvailability {
    // disponibilidad semana
    private  List<WorkingHours> workingHoursList = new ArrayList<>();
    private  List<TimeSlot> slots = new ArrayList<>();

    public WeeklyAvailability() {
    }

    public WeeklyAvailability(Collection<TimeSlot> slots, List<WorkingHours> workingHoursList) {
        this.slots = slots == null ? List.of() : List.copyOf(slots);
        this.workingHoursList = workingHoursList;
    }
    /** es útil como métrica neutra (cuántas horas de slots hay). */
    public int totalHoras()
    {
        return slots.stream().mapToInt(TimeSlot::duracionHoras).sum();
    }


    /**
     * Verifica si el total de horas registradas en la semana cumple con el mínimo requerido.
     * @param //minimoHoras Número mínimo de horas requeridas.
     * @return true si cumple, false si no.
     /
    public boolean HorasRegistradas(int minimoHoras) {
        int totalHoras = workingHoursList.stream()
                .mapToInt(WorkingHours.isCompliantWithWorkingHours())
                .sum();

        return totalHoras >= minimoHoras;
    }*/



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
