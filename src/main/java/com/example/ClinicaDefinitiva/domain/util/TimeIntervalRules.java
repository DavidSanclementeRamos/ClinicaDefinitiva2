package com.example.ClinicaDefinitiva.domain.util;

import java.time.LocalDateTime;

public class TimeIntervalRules {

    //Contiene: A esta completamente dentro de B
    public static boolean contains(LocalDateTime aStart, LocalDateTime bEnd,
                                   LocalDateTime bStart, LocalDateTime aEnd){
        return !aStart.isBefore(bStart) && !aEnd.isBefore(bEnd);
    }

    // Se solapan: A y B tienen al menos una instancia en comun
    public static boolean overlaps(LocalDateTime aStart, LocalDateTime bEnd,
                                   LocalDateTime bStart, LocalDateTime aEnd){
        return !aEnd.isBefore(bStart) && !aStart.isAfter(bEnd);
    }

    // Es adyacente: A termina justo cuando empieza B (o viceversa)
    public static boolean isAdjacent(LocalDateTime aStart, LocalDateTime bEnd,
                                   LocalDateTime bStart, LocalDateTime aEnd){
        return aEnd.equals(bStart) || bEnd.isEqual(aStart);
    }

    // Es igual: A y B tienen exactamente el mismo rango
    public static boolean isEqual(LocalDateTime aStart, LocalDateTime bEnd,
                                   LocalDateTime bStart, LocalDateTime aEnd){
        return aStart.equals(bStart) && aEnd.equals(bEnd);
    }
    // Es valido: start < end
    public static boolean isValid(LocalDateTime start, LocalDateTime end){
        return start != null && end != null && start.isBefore(end);
    }
}
