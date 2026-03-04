
package com.example.ClinicaDefinitiva.domain.adminitration.operations.service;

import com.example.ClinicaDefinitiva.application.exceptions.ProvidedServiceNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.service.ShiftAssignmentService;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShiftAssignmentServiceTest {

    private DentistRepository dentistRepository;
    private ShiftRepository shiftRepository;
    private ShiftAssignmentService service;

    private DentistId dentistId;
    private Dentist dentist;

    @BeforeEach
    void setUp() {
        dentistRepository = mock(DentistRepository.class);
        shiftRepository = mock(ShiftRepository.class);
        service = new ShiftAssignmentService(dentistRepository, shiftRepository);

        dentistId = DentistId.of(1L);
        dentist = mock(Dentist.class);
    }

    @Test
    void shouldAssignShiftSuccessfully() {
        LocalDate date = LocalDate.of(2026, 3, 1);
        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(12, 0);

        WorkingHours workingHours = mock(WorkingHours.class);
        when(dentist.getWorkingHours()).thenReturn(workingHours);
        when(workingHours.isWithinRange(start, end, date.getDayOfWeek())).thenReturn(true);

        when(dentistRepository.findById(dentistId)).thenReturn(Optional.of(dentist));

        Shift expectedShift = Shift.create( dentistId, date, start, end, ShiftType.ADMINISTRATIVE);
        when(shiftRepository.save(any(Shift.class))).thenReturn(expectedShift);

        Shift result = service.assignShift(dentistId, date, start, end, ShiftType.CLINICAL);

        assertNotNull(result);
        assertEquals(dentistId, result.getDentistId());
        assertEquals(date, result.getDate());
        assertEquals(start, result.getStartTime());
        assertEquals(end, result.getEndTime());
        assertTrue(result.isActive());
    }

    @Test
    void shouldThrowExceptionWhenDentistNotFound() {
        when(dentistRepository.findById(dentistId)).thenReturn(Optional.empty());

        assertThrows(ProvidedServiceNotFoundException.class,
            () -> service.assignShift(dentistId,
                    LocalDate.of(2026, 3, 1),
                    LocalTime.of(8, 0),
                    LocalTime.of(12, 0),
                    ShiftType.ON_CALL));
    }

    @Test
    void shouldThrowExceptionWhenOutsideWorkingHours() {
        LocalDate date = LocalDate.of(2026, 3, 1);
        LocalTime start = LocalTime.of(20, 0);
        LocalTime end = LocalTime.of(22, 0);

        WorkingHours workingHours = mock(WorkingHours.class);
        when(dentist.getWorkingHours()).thenReturn(workingHours);
        when(workingHours.isWithinRange(start, end, date.getDayOfWeek())).thenReturn(false);

        when(dentistRepository.findById(dentistId)).thenReturn(Optional.of(dentist));

        assertThrows(BusinessRuleViolationException.class,
            () -> service.assignShift(dentistId, date, start, end, ShiftType.ADMINISTRATIVE));
    }
}

