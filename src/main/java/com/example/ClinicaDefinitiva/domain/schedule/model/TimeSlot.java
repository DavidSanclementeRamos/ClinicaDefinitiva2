package com.example.ClinicaDefinitiva.domain.schedule.model;

import com.example.ClinicaDefinitiva.Enum.Estado;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;

public final class TimeSlot {
    // horario

    private final DayOfWeek dayOfWeek;
    private final LocalTime inicio;
    private final LocalTime fin;

    public TimeSlot(DayOfWeek dayOfWeek, LocalTime inicio, LocalTime fin) {
        if (dayOfWeek == null || inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new IllegalArgumentException("Invalid TimeSlot.");
        }

        this.dayOfWeek = dayOfWeek;
        this.inicio = inicio;
        this.fin = fin;
    }


    // reglas
    public int duracionHoras(){

        return (int) Duration.between(inicio, fin).toHours();
    }

    // para verificar si una fecha ya esta ciendo usadad, asi ebitar el acomplamiento
    public boolean seSolapaCon(TimeSlot otro){
        if (otro == null || otro.dayOfWeek != this.dayOfWeek) return false;
        // se solapan si inicio < otro.fin y fin > otro.inicio
        return !this.inicio.isAfter(otro.fin) && !this.fin.isBefore(otro.inicio);
    }

    public void update () {
        if (!tinecitasFuturas().isEmpty()) {
            throw new ReglaNegocioException("No se puede desactivarse: tiene " + tinecitasFuturas().size() + " citas futuras");
        }

    }


    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public LocalTime getInicio() { return inicio; }
    public LocalTime getFin() { return fin; }




}
