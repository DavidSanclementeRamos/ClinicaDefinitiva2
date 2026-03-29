package com.example.ClinicaDefinitiva.domain.authentication.vo;

import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

class UserIdentityStatusTest {

    @ParameterizedTest
    @EnumSource(UserIdentityStatus.Status.class)
    @DisplayName("Crear UserIdentityStatus con todos los estados")
    void shouldCreateAllStatuses(UserIdentityStatus.Status status) {
        UserIdentityStatus userStatus = UserIdentityStatus.of(status);
        assertThat(userStatus.getValue()).isEqualTo(status);
    }

    @Test
    @DisplayName("Crear con null lanza excepción")
    void shouldThrowForNull() {
        assertThatThrownBy(() -> UserIdentityStatus.of(null))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Transiciones válidas: PENDING_VERIFICATION → ACTIVE")
    void shouldTransitionPendingToActive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.PENDING_VERIFICATION);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.ACTIVE)).isTrue();
        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.ACTIVE);
        assertThat(newStatus.getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
    }

    @Test
    @DisplayName("Transiciones válidas: ACTIVE → INACTIVE")
    void shouldTransitionActiveToInactive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.INACTIVE)).isTrue();
        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.INACTIVE);
        assertThat(newStatus.getValue()).isEqualTo(UserIdentityStatus.Status.INACTIVE);
    }

    @Test
    @DisplayName("Transiciones válidas: ACTIVE → SUSPENDED")
    void shouldTransitionActiveToSuspended() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.SUSPENDED)).isTrue();
        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.SUSPENDED);
        assertThat(newStatus.getValue()).isEqualTo(UserIdentityStatus.Status.SUSPENDED);
    }

    @Test
    @DisplayName("Transiciones válidas: INACTIVE → ACTIVE")
    void shouldTransitionInactiveToActive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.INACTIVE);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.ACTIVE)).isTrue();
        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.ACTIVE);
        assertThat(newStatus.getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
    }

    @Test
    @DisplayName("Transiciones válidas: SUSPENDED → ACTIVE")
    void shouldTransitionSuspendedToActive() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.SUSPENDED);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.ACTIVE)).isTrue();
        UserIdentityStatus newStatus = status.transitionTo(UserIdentityStatus.Status.ACTIVE);
        assertThat(newStatus.getValue()).isEqualTo(UserIdentityStatus.Status.ACTIVE);
    }

    @Test
    @DisplayName("Transición inválida: ACTIVE → PENDING_VERIFICATION")
    void shouldNotTransitionActiveToPending() {
        UserIdentityStatus status = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertThat(status.canTransitionTo(UserIdentityStatus.Status.PENDING_VERIFICATION)).isFalse();
        assertThatThrownBy(() -> status.transitionTo(UserIdentityStatus.Status.PENDING_VERIFICATION))
                .isInstanceOf(ValueObjectValidationException.class);
    }

    @Test
    @DisplayName("Métodos semánticos")
    void testSemanticMethods() {
        UserIdentityStatus active = UserIdentityStatus.of(UserIdentityStatus.Status.ACTIVE);
        assertThat(active.isActive()).isTrue();
        assertThat(active.isInactive()).isFalse();
        assertThat(active.isSuspended()).isFalse();
        assertThat(active.isPendingVerification()).isFalse();

        UserIdentityStatus inactive = UserIdentityStatus.of(UserIdentityStatus.Status.INACTIVE);
        assertThat(inactive.isInactive()).isTrue();

        UserIdentityStatus pending = UserIdentityStatus.of(UserIdentityStatus.Status.PENDING_VERIFICATION);
        assertThat(pending.isPendingVerification()).isTrue();
    }
}
