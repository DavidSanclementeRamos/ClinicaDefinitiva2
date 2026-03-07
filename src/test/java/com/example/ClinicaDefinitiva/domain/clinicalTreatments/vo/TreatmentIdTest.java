
package com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class TreatmentIdTest {

    @Test
    void shouldCreateTreatmentIdSuccessfully() {
        TreatmentId id = TreatmentId.of(10L);
        assertEquals(10L, id.getValue());
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        assertThrows(ValueObjectValidationException.class,
                () -> TreatmentId.of(null));
    }

    @Test
    void shouldBeEqualWhenValuesAreSame() {
        TreatmentId id1 = TreatmentId.of(20L);
        TreatmentId id2 = TreatmentId.of(20L);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        TreatmentId id1 = TreatmentId.of(30L);
        TreatmentId id2 = TreatmentId.of(40L);

        assertNotEquals(id1, id2);
    }
}
