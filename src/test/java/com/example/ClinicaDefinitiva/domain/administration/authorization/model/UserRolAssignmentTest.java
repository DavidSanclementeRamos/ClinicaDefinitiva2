package com.example.ClinicaDefinitiva.domain.administration.authorization.model;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class UserRolAssignmentTest {

    private static final UserIdentityId USER_ID = UserIdentityId.from(1L);
    private static final RolId ROL_ID = RolId.of(1L);

    @Test
    @DisplayName("AUTH-UNIT-006: Asignación permanente")
    void assignPermanent() {
        UserRolAssignment assignment = UserRolAssignment.assignPermanent(USER_ID, ROL_ID, true);

        assertThat(assignment.getUserId()).isEqualTo(USER_ID);
        assertThat(assignment.getRolId()).isEqualTo(ROL_ID);
        assertThat(assignment.isPrimary()).isTrue();
        assertThat(assignment.getValidTo()).isNull();
        assertThat(assignment.isCurrentlyActive()).isTrue();
    }

    @Test
    @DisplayName("Asignación temporal válida")
    void assignTemporary() {
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(3);
        UserRolAssignment assignment = UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false);

        assertThat(assignment.isPrimary()).isFalse();
        assertThat(assignment.getValidFrom()).isEqualTo(validFrom);
        assertThat(assignment.getValidTo()).isEqualTo(validTo);
        assertThat(assignment.isCurrentlyActive()).isTrue();
    }

    @Test
    @DisplayName("Asignación temporal no puede ser primaria")
    void temporaryCannotBePrimary() {
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(3);

        assertThatThrownBy(() -> UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, true))
                .isInstanceOf(BusinessRuleViolationException.class);
    }

    @Test
    @DisplayName("Validar fechas: validFrom no puede ser null")
    void validFromRequired() {
        assertThatThrownBy(() -> UserRolAssignment.assignTemporary(USER_ID, ROL_ID, null, LocalDate.now(), false))
                .isInstanceOf(DomainAggregateException.class);
    }

    @Test
    @DisplayName("Validar fechas: validTo no puede ser anterior a validFrom")
    void validToCannotBeBeforeValidFrom() {
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.minusDays(1);

        assertThatThrownBy(() -> UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false))
                .isInstanceOf(DomainAggregateException.class);
    }

    @Test
    @DisplayName("isActiveAt: fecha dentro del rango")
    void isActiveAt_withinRange() {
        LocalDate validFrom = LocalDate.now().minusDays(5);
        LocalDate validTo = validFrom.plusMonths(2);
        UserRolAssignment assignment = UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false);

        assertThat(assignment.isActiveAt(validFrom.plusDays(10))).isTrue();
        assertThat(assignment.isActiveAt(validFrom)).isTrue();
        assertThat(assignment.isActiveAt(validTo)).isTrue();
    }

    @Test
    @DisplayName("isActiveAt: fecha fuera del rango")
    void isActiveAt_outsideRange() {
        LocalDate validFrom = LocalDate.now().minusDays(5);
        LocalDate validTo = validFrom.plusMonths(2);
        UserRolAssignment assignment = UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false);

        assertThat(assignment.isActiveAt(validFrom.minusDays(1))).isFalse();
        assertThat(assignment.isActiveAt(validTo.plusDays(1))).isFalse();
    }

    @Test
    @DisplayName("Extender asignación temporal")
    void extend() {
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(3);
        UserRolAssignment assignment = UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false);

        LocalDate newValidTo = validTo.plusMonths(2);
        assignment.extend(newValidTo);

        assertThat(assignment.getValidTo()).isEqualTo(newValidTo);
    }

    @Test
    @DisplayName("Extender asignación permanente lanza excepción")
    void extendPermanent_throws() {
        UserRolAssignment assignment = UserRolAssignment.assignPermanent(USER_ID, ROL_ID, true);

        assertThatThrownBy(() -> assignment.extend(LocalDate.now().plusMonths(1)))
                .isInstanceOf(DomainAggregateException.class);
    }

    @Test
    @DisplayName("Extender con fecha anterior lanza excepción")
    void extendWithEarlierDate_throws() {
        LocalDate validFrom = LocalDate.now();
        LocalDate validTo = validFrom.plusMonths(3);
        UserRolAssignment assignment = UserRolAssignment.assignTemporary(USER_ID, ROL_ID, validFrom, validTo, false);

        assertThatThrownBy(() -> assignment.extend(validTo.minusDays(1)))
                .isInstanceOf(DomainAggregateException.class);
    }

    @Test
    @DisplayName("Revocar asignación")
    void revoke() {
        UserRolAssignment assignment = UserRolAssignment.assignPermanent(USER_ID, ROL_ID, true);
        assignment.revoke();

        assertThat(assignment.getValidTo()).isEqualTo(LocalDate.now().minusDays(1));
        assertThat(assignment.isCurrentlyActive()).isFalse();
    }
}