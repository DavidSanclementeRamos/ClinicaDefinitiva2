
package com.example.ClinicaDefinitiva.domain.dentalService.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorService.ServiceVOError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;
import java.util.Objects;

public final class AgeRange {

    private final int minAge;
    private final int maxAge;

    /**
     * Constructor controlado con validaciones.
     *
     * @param minAge Edad mínima.
     * @param maxAge Edad máxima.
     */
    private AgeRange(int minAge, int maxAge) {
        if (minAge < 0) {
            throw new ValueObjectValidationException(
                ServiceVOError.ERR_SERVICE_INVALID_MIN_AGE,
                VOContext.DENTAL_SERVICES
            );
        }
        if (maxAge <= minAge) {
            throw new ValueObjectValidationException(
                ServiceVOError.ERR_SERVICE_INVALID_RANGE,
                VOContext.DENTAL_SERVICES
            );
        }
        this.minAge = minAge;
        this.maxAge = maxAge;
    }
    
     public  static AgeRange of(int minAge, int maxAge){
         return new AgeRange(minAge,maxAge);
     }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        return minAge + "-" + maxAge + " años";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgeRange)) return false;
        AgeRange that = (AgeRange) o;
        return minAge == that.minAge && maxAge == that.maxAge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minAge, maxAge);
    }
}

