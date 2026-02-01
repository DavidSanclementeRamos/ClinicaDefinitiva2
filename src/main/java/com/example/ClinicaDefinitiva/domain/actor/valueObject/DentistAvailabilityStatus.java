package com.example.ClinicaDefinitiva.domain.actor.valueObject;


import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.VoActorError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

public final class DentistAvailabilityStatus {
    public enum Status
    {

        AVAILABLE,
        UNAVAILABLE,
        ON_BREAK,
        IN_CONSULTATION,
        OFF_SHIFT,
        ON_CALL,
        SICK_LEAVE,
        VACATION,
        TRAINING,
        ADMIN_TASK


    }

    private Status current;

    private static final EnumMap<Status, Set<Status>> validTransitions = new EnumMap<>(Status.class);

    static {
        validTransitions.put(Status.AVAILABLE, EnumSet.of(Status.IN_CONSULTATION, Status.ON_BREAK, Status.UNAVAILABLE, Status.OFF_SHIFT));
        validTransitions.put(Status.IN_CONSULTATION, EnumSet.of(Status.ON_BREAK, Status.AVAILABLE));
        validTransitions.put(Status.ON_BREAK, EnumSet.of(Status.AVAILABLE, Status.UNAVAILABLE));
        validTransitions.put(Status.UNAVAILABLE, EnumSet.of(Status.AVAILABLE, Status.OFF_SHIFT));
        validTransitions.put(Status.OFF_SHIFT, EnumSet.of(Status.AVAILABLE, Status.ON_CALL));
        validTransitions.put(Status.ON_CALL, EnumSet.of(Status.AVAILABLE));
        validTransitions.put(Status.SICK_LEAVE, EnumSet.of(Status.UNAVAILABLE));
        validTransitions.put(Status.VACATION, EnumSet.of(Status.UNAVAILABLE));
        validTransitions.put(Status.TRAINING, EnumSet.of(Status.AVAILABLE, Status.ADMIN_TASK));
        validTransitions.put(Status.ADMIN_TASK, EnumSet.of(Status.AVAILABLE));
    }

    public DentistAvailabilityStatus(Status initialStatus) {
        this.current = initialStatus;
    }

    public Status getCurrent() {
        return current;
    }

    public static DentistAvailabilityStatus from(DentistAvailabilityStatus.Status value) {
        if (value == null) {
            throw new ValueObjectValidationException(VoActorError.ERR_AVAILABILITY_STATUS_TRANSITION_NULL, VOContext.DENTIST_AVAILABILITY_STATUS);
        }
        return new DentistAvailabilityStatus (value);
    }

    public boolean canTransitionTo(Status next) {
        return validTransitions.getOrDefault(current, EnumSet.noneOf(Status.class)).contains(next);
    }

    public boolean tryTransitionTo(Status next) {
        if (canTransitionTo(next)) {
            this.current = next;
            return true;
        }
        return false;
    }
    // Semántica avanzada
    /*
      Consultar si el profesional está operativamente disponible (isOperational)
 	  Detectar si está temporalmente no disponible (isTemporarilyUnavailable)
	  Obtener un nivel de prioridad para asignación inteligente (isPermanentlyUnavailable)
      Esto te será útil para dashboards, lógica de asignación, o motores de recomendación.
     */
    public boolean isOperational() {
        return current == Status.AVAILABLE ||  current == Status.ON_CALL || current  == Status.TRAINING ||current  == Status.ADMIN_TASK;
    }

    public boolean isTemporarilyUnavailable() {
        return current == Status.ON_BREAK ||  current == Status.IN_CONSULTATION;
    }

    public boolean isPermanentlyUnavailable() {
        return  current == Status.UNAVAILABLE || current == Status.SICK_LEAVE || current == Status.VACATION || current == Status.OFF_SHIFT;
    }

    // Nivel de prioridad para asignación (0 = no asignable, 3 = alta prioridad)
    public int getPriorityLevel() {
        return switch (current) {
            case AVAILABLE       -> 3;
            case ON_CALL         -> 2;
            case TRAINING,
                 ADMIN_TASK      -> 1;
            case ON_BREAK,
                 IN_CONSULTATION -> 0;
            default              -> -1; // No asignable
        };
    }



    @Override
    public String toString() {
        return current.name();
    }





}
