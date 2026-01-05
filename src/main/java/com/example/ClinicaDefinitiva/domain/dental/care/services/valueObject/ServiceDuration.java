package com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object: Duración de un servicio clínico
 * Propósito: Representar la duración de un servicio/cita en minutos
 * Validaciones: Debe ser positiva y dentro de límites razonables
 */
public final class ServiceDuration {

    private final int minutes;

    // Constantes de negocio
    private static final int MIN_DURATION_MINUTES = 15;    // Duración mínima: 15 minutos
    private static final int MAX_DURATION_MINUTES = 480;   // Duración máxima: 8 horas

    private ServiceDuration(int minutes) {
        validateDuration(minutes);
        this.minutes = minutes;
    }


    /**
     * Crea una duración desde minutos
     */
    public static ServiceDuration of(int minutes) {
        return new ServiceDuration(minutes);
    }

    /**
     * Crea una duración desde horas
     */
    public static ServiceDuration ofHours(int hours) {
        return new ServiceDuration(hours * 60);
    }

    /**
     * Crea una duración calculada entre dos fechas/horas
     */
    public static ServiceDuration between(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_DURATION_START_END_REQUIRED, VOContext.SERVICE_DURATION);
        }
        if (!start.isBefore(end)) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_DURATION_START_BEFORE_END,VOContext.SERVICE_DURATION);
        }

        long minutes = Duration.between(start, end).toMinutes();
        return new ServiceDuration((int) minutes);
    }

    /**
     * Crea una duración desde Java Duration
     */
    public static ServiceDuration from(Duration duration) {
        if (duration == null) {
            throw new ValueObjectValidationException(ServiceVOError.ERR_SERVICE_DURATION_REQUIRED,VOContext.SERVICE_DURATION);
        }
        return new ServiceDuration((int) duration.toMinutes());
    }

    //  VALIDACIONES

    private void validateDuration(int minutes) {
        if (minutes <= 0) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_DURATION_POSITIVE,VOContext.SERVICE_DURATION
            );
        }

        if (minutes < MIN_DURATION_MINUTES) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_DURATION_MINIMUM,VOContext.SERVICE_DURATION);
        }

        if (minutes > MAX_DURATION_MINUTES) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_DURATION_MAXIMUM,VOContext.SERVICE_DURATION);
        }
    }

    // OPERACIONES

    /**
     * Suma otra duración
     */
    public ServiceDuration plus(ServiceDuration other) {
        return new ServiceDuration(this.minutes + other.minutes);
    }

    /**
     * Resta otra duración
     */
    public ServiceDuration minus(ServiceDuration other) {
        int result = this.minutes - other.minutes;
        if (result <= 0) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_DURATION_RESULT_POSITIVE,VOContext.SERVICE_DURATION
            );
        }
        return new ServiceDuration(result);
    }

    /**
     * Multiplica la duración
     */
    public ServiceDuration multiply(int factor) {
        if (factor <= 0) {
            throw new ValueObjectValidationException(ServiceVOError
                    .ERR_SERVICE_DURATION_FACTOR_POSITIVE,VOContext.SERVICE_DURATION);
        }
        return new ServiceDuration(this.minutes * factor);
    }

    /**
     * Convierte a Java Duration
     */
    public Duration toDuration() {
        return Duration.ofMinutes(minutes);
    }

    //  QUERIES

    /**
     * Verifica si es una duración corta (< 30 minutos)
     */
    public boolean isShort() {
        return minutes < 30;
    }

    /**
     * Verifica si es una duración larga (>= 2 horas)
     */
    public boolean isLong() {
        return minutes >= 120;
    }

    /**
     * Verifica si es múltiplo de 15 minutos
     */
    public boolean isStandardSlot() {
        return minutes % 15 == 0;
    }

    /**
     * Compara con otra duración
     */
    public boolean isLongerThan(ServiceDuration other) {
        return this.minutes > other.minutes;
    }

    public boolean isShorterThan(ServiceDuration other) {
        return this.minutes < other.minutes;
    }

    public boolean isEqualTo(ServiceDuration other) {
        return this.minutes == other.minutes;
    }

    // GETTERS

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return minutes / 60;
    }

    public int getRemainingMinutes() {
        return minutes % 60;
    }

    /**
     * Formato legible: "1h 30m" o "45m"
     */
    public String toReadableFormat() {
        if (minutes < 60) {
            return minutes + "m";
        }

        int hours = getHours();
        int remainingMinutes = getRemainingMinutes();

        if (remainingMinutes == 0) {
            return hours + "h";
        }

        return hours + "h " + remainingMinutes + "m";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceDuration)) return false;
        ServiceDuration that = (ServiceDuration) o;
        return minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }

    @Override
    public String toString() {
        return toReadableFormat();
    }
}
