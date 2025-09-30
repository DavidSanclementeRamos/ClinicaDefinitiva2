package com.example.ClinicaDefinitiva.domain.schedule.model;

import java.util.List;

public class Availability {
    // disponibilidad

    private List<TimeSlot> timeSlotList;

    public Availability() {
    }

    public Availability(List<TimeSlot> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public int horasTotales(){
        return timeSlotList.stream()
                .mapToInt(TimeSlot::duracionHoras)
                .sum();
    }

    public boolean cumpleMinimoSemanal(int minimoHoras) {
        return horasTotales() >= minimoHoras;
    }

    public boolean tieneSolapamientos() {
        for (int i = 0; i < timeSlotList.size(); i++) {
            for (int j = i + 1; j < timeSlotList.size(); j++) {
                if (timeSlotList.get(i).seSolapaCon(timeSlotList.get(j))) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<TimeSlot> getTimeSlotList() {
        return timeSlotList;
    }

    public void setTimeSlotList(List<TimeSlot> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }
}




