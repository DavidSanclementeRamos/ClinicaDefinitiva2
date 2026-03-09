
package com.example.ClinicaDefinitiva.domain.administration.operations.vo;

import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ExcludedBlock;
import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.operations.OperationsVoError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ExcludedBlockTest {

    @Test
    void shouldThrowExceptionWhenStartIsAfterEnd() {
        LocalTime start = LocalTime.of(13, 0);
        LocalTime end = LocalTime.of(12, 0);

        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> new ExcludedBlock(start, end, "Invalid block"));

        assertEquals(OperationsVoError.ERR_EXCLUDED_BLOCK_INVALID_RANGE, ex.getCatalogo());
        assertEquals(VOContext.OPERATIONS, ex.getContexto());
    }

    @Test
    void shouldThrowExceptionWhenStartEqualsEnd() {
        LocalTime start = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(12, 0);

        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> new ExcludedBlock(start, end, "Zero duration"));

        assertEquals(OperationsVoError.ERR_EXCLUDED_BLOCK_INVALID_RANGE, ex.getCatalogo());
    }

    @Test
    void shouldThrowExceptionWhenStartOrEndIsNull() {
        ValueObjectValidationException ex = assertThrows(ValueObjectValidationException.class,
            () -> new ExcludedBlock(null, LocalTime.of(13, 0), "Null start"));

        assertEquals(OperationsVoError.ERR_EXCLUDED_BLOCK_NULL_TIME, ex.getCatalogo());
    }

    @Test
    void shouldCreateValidBlock() {
        ExcludedBlock lunch = new ExcludedBlock(LocalTime.of(12, 0), LocalTime.of(13, 0), "Lunch");
        assertEquals(Duration.ofHours(1), lunch.getDuration());
        assertEquals("Lunch", lunch.getReason());
    }

    @Test
    void shouldDetectOverlapBetweenBlocks() {
        ExcludedBlock lunch = new ExcludedBlock(LocalTime.of(12, 0), LocalTime.of(13, 0), "Lunch");
        ExcludedBlock meeting = new ExcludedBlock(LocalTime.of(12, 30), LocalTime.of(13, 30), "Meeting");

        assertTrue(lunch.overlapsWith(meeting));
    }
}
