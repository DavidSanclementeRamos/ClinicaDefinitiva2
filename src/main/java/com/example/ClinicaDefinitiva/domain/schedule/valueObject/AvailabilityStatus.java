package com.example.ClinicaDefinitiva.domain.schedule.valueObject;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public class AvailabilityStatus {
    public enum Status {
        FREE,
        BOOKED,
        BLOCKED,
        CANCELLED,
        EXPIRED,
        PENDING,
        OVERLAPPED,
        LOCKED
    }


    private  Status value;
    private static final EnumMap<Status, Set<Status>> validTransitions = new EnumMap<>(Status.class);

    static {
        validTransitions.put(Status.FREE, EnumSet.of(Status.BOOKED, Status.BLOCKED, Status.LOCKED, Status.PENDING));
        validTransitions.put(Status.BOOKED, EnumSet.of(Status.CANCELLED, Status.EXPIRED));
        validTransitions.put(Status.BLOCKED, EnumSet.of(Status.FREE, Status.LOCKED));
        validTransitions.put(Status.CANCELLED, EnumSet.of(Status.FREE));
        validTransitions.put(Status.EXPIRED, EnumSet.noneOf(Status.class));
        validTransitions.put(Status.PENDING, EnumSet.of(Status.BOOKED, Status.CANCELLED));
        validTransitions.put(Status.OVERLAPPED, EnumSet.of(Status.FREE));
        validTransitions.put(Status.LOCKED, EnumSet.of(Status.FREE));
    }



  /**
  Propósito: Verifica si el estado actual (value) puede cambiar válidamente al estado next.
 	Cómo lo hace:
 	Busca en el mapa validTransitions el conjunto de estados permitidos desde el estado actual.
 	Si no hay reglas definidas, usa un conjunto vacío (EnumSet.noneOf).
 	Verifica si next está dentro de ese conjunto.
 	Resultado: true si la transición es válida, false si no lo es.
  **/

    public boolean canTransitionTo(Status next) {
        return validTransitions.getOrDefault(value, EnumSet.noneOf(Status.class)).contains(next);
    }

    /** 	Propósito: Intenta realizar la transición de estado.
 	Cómo lo hace:
 	Usa canTransitionTo para validar si el cambio es permitido.
 	Si lo es, actualiza el estado interno (value) al nuevo estado next.
 	Resultado: true si la transición fue exitosa, false si fue rechazada.
    Este patrón encapsula la lógica de negocio y evita transiciones inválidas,
     lo cual es clave para mantener la trazabilidad y consistencia del sistema.
    **/
    public boolean tryTransitionTo(Status next) {
        if (canTransitionTo(next)) {
            this.value = next;
            return true;
        }
        return false;
    }



    private AvailabilityStatus(Status value) {
        this.value = value;
    }

    /**
     * Propósito: Método de fábrica para crear una instancia de AvailabilityStatus desde un Status.
     * • 	Validación: Lanza excepción si el valor es null , protegiendo la integridad del VO.
     * • 	Resultado: Una instancia segura y válida de AvailabilityStatus.
     */
    public static AvailabilityStatus from(Status value) {
        if (value == null) {
            throw new IllegalArgumentException("Availability status cannot be null.");
        }
        return new AvailabilityStatus(value);
    }

    public Status getValue() {
        return value;
    }

    /**
     * 	Propósito: Proveen una interfaz semántica para consultar el estado sin exponer directamente el enum.
     * • 	Ventaja:
     * • 	Mejora la legibilidad del código (if(status.isAvailable()) en lugar de if(status.getValue()== status.isAvailable ).
     * • 	Facilita refactorizaciones y encapsulamiento.
     * • 	Permite extender lógica futura sin romper contratos externos.
     * **/

    // Semántica avanzada

    /**
     *	Saber si un bloque es reservable (isReservable)
     * • 	Detectar si está en conflicto (isConflictive)
     * • 	Obtener un peso de asignación (isFinalState) para priorizar slots en motores de recomendación
     * **/
    public boolean isReservable() {
        return value == Status.FREE || value == Status.PENDING;
    }

    public boolean isConflictive() {
        return value == Status.OVERLAPPED || value == Status.LOCKED || value == Status.BLOCKED;
    }

    public boolean isFinalState() {
        return value == Status.EXPIRED || value == Status.CANCELLED;
    }

    // Peso para motores de asignación (0 = no asignable, 3 = alta prioridad)
    public int getBookingWeight() {
        return switch (value) {
            case FREE       -> 3;
            case PENDING    -> 2;
            case BOOKED     -> 1;
            default         -> 0;
        };
    }






public boolean isFree() {
        return value == Status.FREE;
    }

    public boolean isBlocked() {
        return value == Status.BLOCKED;
    }

    public boolean isBooked() {
        return value == Status.BOOKED;
    }

    public boolean isExpired() {
        return value == Status.EXPIRED;
    }

    public boolean isCancelled() {
        return value == Status.CANCELLED;
    }
    public boolean isLocked() {
        return value == Status.LOCKED;
    }
    public boolean isOverlapped() {
        return value == Status.OVERLAPPED;
    }
    public boolean isPending() {
        return value == Status.PENDING;
    }

    @Override
    public String toString() {
        return value.name();
    }



}
