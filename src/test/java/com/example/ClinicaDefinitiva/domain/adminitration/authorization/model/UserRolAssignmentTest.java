
package com.example.ClinicaDefinitiva.domain.adminitration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class UserRolAssignmentTest {

    @Test
    void shouldCreatePermanentAssignment() {
        UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                UserIdentityId.from(1L),
                RolId.of(10L),
                true
        );

        assertTrue(assignment.isPrimary());
        assertNull(assignment.getValidTo());
        assertTrue(assignment.isCurrentlyActive());
    }

    @Test
    void shouldCreateTemporaryAssignment() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(10);

        UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                UserIdentityId.from(2L),
                RolId.of(20L),
                start,
                end,
                false
        );

        assertFalse(assignment.isPrimary());
        assertTrue(assignment.isActiveAt(start.plusDays(5)));
        assertFalse(assignment.isActiveAt(end.plusDays(1)));
    }

    @Test
    void shouldThrowWhenTemporaryAssignmentIsPrimary() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);

        assertThrows(BusinessRuleViolationException.class, () ->
                UserRolAssignment.assignTemporary(
                        UserIdentityId.from(3L),
                        RolId.of(30L),
                        start,
                        end,
                        true
                )
        );
    }

    @Test
    void shouldExtendTemporaryAssignment() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(5);

        UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                UserIdentityId.from(4L),
                RolId.of(40L),
                start,
                end,
                false
        );

        LocalDate newEnd = end.plusDays(5);
        assignment.extend(newEnd);

        assertEquals(newEnd, assignment.getValidTo());
    }

    @Test
    void shouldThrowWhenExtendingPermanentAssignment() {
        UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                UserIdentityId.from(5L),
                RolId.of(50L),
                false
        );

        assertThrows(DomainAggregateException.class, () ->
                assignment.extend(LocalDate.now().plusDays(10))
        );
    }

    @Test
    void shouldRevokeAssignment() {
        LocalDate start = LocalDate.now().minusDays(10);
        LocalDate end = LocalDate.now().plusDays(10);

        UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                UserIdentityId.from(6L),
                RolId.of(60L),
                start,
                end,
                false
        );

        assignment.revoke();
        assertTrue(assignment.getValidTo().isBefore(LocalDate.now()));
    }
}
