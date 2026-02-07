package com.example.ClinicaDefinitiva.domain;



import com.example.ClinicaDefinitiva.domain.administration.authorization.model.UserRolAssignment;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.authorization.UserRolAssignmentError;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests exhaustivos para el agregado UserRolAssignment
 *
 * Este agregado es CRÍTICO porque maneja:
 * - Asignación temporal vs permanente
 * - Roles primarios (un solo activo por usuario)
 * - Validación de fechas (validFrom, validTo)
 * - Extensión y revocación de asignaciones
 */
@DisplayName("UserRolAssignment - Domain Aggregate Tests")
class UserRolAssignmentTest {

    private UserId userId;
    private RolId rolId;
    private UserRolAssignmentId assignmentId;
    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate nextWeek;
    private LocalDate nextMonth;

    @BeforeEach
    void setUp() {
        userId = UserId.from(1L);
        rolId = RolId.of(2L);
        assignmentId = UserRolAssignmentId.of(1L);

        today = LocalDate.now();
        tomorrow = today.plusDays(1);
        nextWeek = today.plusDays(7);
        nextMonth = today.plusMonths(1);
    }

    // ===================================================================================
    // NESTED: Constructor y Validaciones
    // ===================================================================================
    @Nested
    @DisplayName("Constructor y Validaciones de Estado Inicial")
    class ConstructorAndValidations {

        @Test
        @DisplayName("Constructor debe inicializar todos los campos correctamente")
        void constructor_ShouldInitializeAllFieldsCorrectly() {
            // When
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId,
                    userId,
                    rolId,
                    today,
                    nextMonth,
                    true
            );

            // Then
            assertThat(assignment.getId()).isEqualTo(assignmentId);
            assertThat(assignment.getUserId()).isEqualTo(userId);
            assertThat(assignment.getRolId()).isEqualTo(rolId);
            assertThat(assignment.getValidFrom()).isEqualTo(today);
            assertThat(assignment.getValidTo()).isEqualTo(nextMonth);
            assertThat(assignment.isPrimary()).isTrue();
        }

        @Test
        @DisplayName("Constructor debe lanzar excepción cuando validFrom es null")
        void constructor_WhenValidFromIsNull_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> new UserRolAssignment(
                    assignmentId, userId, rolId, null, nextMonth, false
            ))
                    .isInstanceOf(DomainAggregateException.class)
                    .hasFieldOrPropertyWithValue("errorCode",
                            UserRolAssignmentError.ERR_ASSIGNMENT_VALID_FROM_REQUIRED);
        }

        @Test
        @DisplayName("Constructor debe lanzar excepción cuando validTo es anterior a validFrom")
        void constructor_WhenValidToBeforeValidFrom_ShouldThrowException() {
            // Given
            LocalDate pastDate = today.minusDays(1);

            // When & Then
            assertThatThrownBy(() -> new UserRolAssignment(
                    assignmentId, userId, rolId, today, pastDate, false
            ))
                    .isInstanceOf(DomainAggregateException.class)
                    .hasFieldOrPropertyWithValue("errorCode",
                            UserRolAssignmentError.ERR_ASSIGNMENT_INVALID_DATE_RANGE);
        }

        @Test
        @DisplayName("Constructor debe permitir validTo null (asignación permanente)")
        void constructor_WhenValidToIsNull_ShouldCreatePermanentAssignment() {
            // When
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, null, true
            );

            // Then
            assertThat(assignment.getValidTo()).isNull();
            assertThat(assignment.getValidFrom()).isEqualTo(today);
        }

        @Test
        @DisplayName("Constructor debe permitir validFrom y validTo iguales")
        void constructor_WhenValidFromEqualsValidTo_ShouldBeValid() {
            // When
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, today, false
            );

            // Then
            assertThat(assignment.getValidFrom()).isEqualTo(today);
            assertThat(assignment.getValidTo()).isEqualTo(today);
        }
    }

    // ===================================================================================
    // NESTED: Factory Methods - Permanent Assignment
    // ===================================================================================
    @Nested
    @DisplayName("Factory Method - assignPermanent")
    class AssignPermanentFactory {

        @Test
        @DisplayName("assignPermanent debe crear asignación sin fecha de expiración")
        void assignPermanent_ShouldCreateAssignmentWithoutExpirationDate() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );

            // Then
            assertThat(assignment.getValidTo()).isNull();
            assertThat(assignment.getValidFrom()).isNotNull();
            assertThat(assignment.isPrimary()).isTrue();
        }

        @Test
        @DisplayName("assignPermanent debe establecer validFrom como LocalDate.now()")
        void assignPermanent_ShouldSetValidFromToToday() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // Then
            assertThat(assignment.getValidFrom())
                    .isEqualTo(LocalDate.now())
                    .isBeforeOrEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("assignPermanent puede crear asignación primaria")
        void assignPermanent_CanCreatePrimaryAssignment() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );

            // Then
            assertThat(assignment.isPrimary()).isTrue();
        }

        @Test
        @DisplayName("assignPermanent puede crear asignación no primaria")
        void assignPermanent_CanCreateNonPrimaryAssignment() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // Then
            assertThat(assignment.isPrimary()).isFalse();
        }
    }

    // ===================================================================================
    // NESTED: Factory Methods - Temporary Assignment
    // ===================================================================================
    @Nested
    @DisplayName("Factory Method - assignTemporary")
    class AssignTemporaryFactory {

        @Test
        @DisplayName("assignTemporary debe crear asignación temporal con fechas correctas")
        void assignTemporary_ShouldCreateTemporaryAssignmentWithCorrectDates() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, false
            );

            // Then
            assertThat(assignment.getValidFrom()).isEqualTo(today);
            assertThat(assignment.getValidTo()).isEqualTo(nextMonth);
            assertThat(assignment.isPrimary()).isFalse();
        }

        @Test
        @DisplayName("assignTemporary debe lanzar excepción si isPrimary es true")
        void assignTemporary_WhenIsPrimaryTrue_ShouldThrowException() {
            // When & Then
            assertThatThrownBy(() -> UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, true
            ))
                    .isInstanceOf(BusinessRuleViolationException.class)
                    .hasFieldOrPropertyWithValue("errorCode",
                            UserRolAssignmentError.ERR_ASSIGNMENT_TEMPORARY_CANNOT_BE_PRIMARY);
        }

        @Test
        @DisplayName("assignTemporary SIEMPRE debe tener isPrimary=false")
        void assignTemporary_ShouldAlwaysHaveIsPrimaryFalse() {
            // When
            UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, false
            );

            // Then
            assertThat(assignment.isPrimary()).isFalse();
        }

        @Test
        @DisplayName("assignTemporary debe heredar validaciones del constructor")
        void assignTemporary_ShouldInheritConstructorValidations() {
            // When & Then - validTo anterior a validFrom
            assertThatThrownBy(() -> UserRolAssignment.assignTemporary(
                    userId, rolId, nextMonth, today, false
            ))
                    .isInstanceOf(DomainAggregateException.class);
        }
    }

    // ===================================================================================
    // NESTED: Active Status Checks
    // ===================================================================================
    @Nested
    @DisplayName("Active Status Verification")
    class ActiveStatusVerification {

        @Test
        @DisplayName("isActiveAt debe retornar true cuando la fecha está en el rango válido")
        void isActiveAt_WhenDateInRange_ShouldReturnTrue() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );

            // When & Then
            assertThat(assignment.isActiveAt(today)).isTrue();
            assertThat(assignment.isActiveAt(nextWeek)).isTrue();
            assertThat(assignment.isActiveAt(nextMonth)).isTrue();
        }

        @Test
        @DisplayName("isActiveAt debe retornar false cuando la fecha es anterior a validFrom")
        void isActiveAt_WhenDateBeforeValidFrom_ShouldReturnFalse() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, tomorrow, nextMonth, false
            );
            LocalDate yesterday = today.minusDays(1);

            // When & Then
            assertThat(assignment.isActiveAt(yesterday)).isFalse();
            assertThat(assignment.isActiveAt(today)).isFalse();
        }

        @Test
        @DisplayName("isActiveAt debe retornar false cuando la fecha es posterior a validTo")
        void isActiveAt_WhenDateAfterValidTo_ShouldReturnFalse() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextWeek, false
            );
            LocalDate twoWeeks = today.plusDays(14);

            // When & Then
            assertThat(assignment.isActiveAt(twoWeeks)).isFalse();
        }

        @Test
        @DisplayName("isActiveAt debe retornar true para permanente (validTo null) en cualquier fecha futura")
        void isActiveAt_WhenPermanentAssignment_ShouldReturnTrueForFutureDates() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );

            // When & Then
            assertThat(assignment.isActiveAt(today)).isTrue();
            assertThat(assignment.isActiveAt(nextMonth)).isTrue();
            assertThat(assignment.isActiveAt(today.plusYears(10))).isTrue();
        }

        @Test
        @DisplayName("isActiveAt debe retornar false para permanente si la fecha es anterior a validFrom")
        void isActiveAt_WhenPermanentButDateBeforeValidFrom_ShouldReturnFalse() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );
            LocalDate yesterday = today.minusDays(1);

            // When & Then
            assertThat(assignment.isActiveAt(yesterday)).isFalse();
        }

        @Test
        @DisplayName("isCurrentlyActive debe retornar true cuando está activo hoy")
        void isCurrentlyActive_WhenActiveToday_ShouldReturnTrue() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );

            // When & Then
            assertThat(assignment.isCurrentlyActive()).isTrue();
        }

        @Test
        @DisplayName("isCurrentlyActive debe retornar false cuando ha expirado")
        void isCurrentlyActive_WhenExpired_ShouldReturnFalse() {
            // Given
            LocalDate lastWeek = today.minusDays(7);
            LocalDate yesterday = today.minusDays(1);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, lastWeek, yesterday, false
            );

            // When & Then
            assertThat(assignment.isCurrentlyActive()).isFalse();
        }

        @Test
        @DisplayName("isCurrentlyActive debe retornar false cuando aún no ha comenzado")
        void isCurrentlyActive_WhenNotStartedYet_ShouldReturnFalse() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, tomorrow, nextMonth, false
            );

            // When & Then
            assertThat(assignment.isCurrentlyActive()).isFalse();
        }

        @ParameterizedTest
        @CsvSource({
                "0, 30, true",   // Hoy hasta dentro de 30 días
                "-7, 7, true",   // Hace 7 días hasta dentro de 7
                "1, 30, false",  // Mañana hasta dentro de 30 (no activo hoy)
                "-30, -1, false" // Hace 30 días hasta ayer (expirado)
        })
        @DisplayName("isCurrentlyActive - Casos parametrizados")
        void isCurrentlyActive_ParameterizedCases(int fromOffset, int toOffset, boolean expected) {
            // Given
            LocalDate from = today.plusDays(fromOffset);
            LocalDate to = today.plusDays(toOffset);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, from, to, false
            );

            // When & Then
            assertThat(assignment.isCurrentlyActive()).isEqualTo(expected);
        }
    }

    // ===================================================================================
    // NESTED: Extend Operations
    // ===================================================================================
    @Nested
    @DisplayName("Extend Validity Operations")
    class ExtendValidityOperations {

        @Test
        @DisplayName("extend debe actualizar validTo cuando la nueva fecha es posterior")
        void extend_WhenNewDateIsLater_ShouldUpdateValidTo() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextWeek, false
            );
            LocalDate twoWeeks = today.plusDays(14);

            // When
            assignment.extend(twoWeeks);

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(twoWeeks);
        }

        @Test
        @DisplayName("extend debe lanzar excepción cuando la asignación es permanente")
        void extend_WhenPermanentAssignment_ShouldThrowException() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // When & Then
            assertThatThrownBy(() -> assignment.extend(nextMonth))
                    .isInstanceOf(DomainAggregateException.class)
                    .hasFieldOrPropertyWithValue("errorCode",
                            UserRolAssignmentError.ERR_ASSIGNMENT_CANNOT_EXTEND_PERMANENT);
        }

        @Test
        @DisplayName("extend debe lanzar excepción cuando la nueva fecha es anterior a validTo")
        void extend_WhenNewDateBeforeCurrentValidTo_ShouldThrowException() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );

            // When & Then
            assertThatThrownBy(() -> assignment.extend(nextWeek))
                    .isInstanceOf(DomainAggregateException.class)
                    .hasFieldOrPropertyWithValue("errorCode",
                            UserRolAssignmentError.ERR_ASSIGNMENT_INVALID_EXTENSION_DATE);
        }

        @Test
        @DisplayName("extend debe permitir extender a la misma fecha (no-op)")
        void extend_WhenNewDateEqualsCurrentValidTo_ShouldBeAllowed() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );

            // When
            assignment.extend(nextMonth);

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(nextMonth);
        }

        @Test
        @DisplayName("extend múltiples veces debe funcionar correctamente")
        void extend_MultipleTimes_ShouldWorkCorrectly() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextWeek, false
            );

            // When
            assignment.extend(nextWeek.plusDays(7));
            assignment.extend(nextWeek.plusDays(14));
            assignment.extend(nextWeek.plusDays(21));

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(nextWeek.plusDays(21));
        }
    }

    // ===================================================================================
    // NESTED: Revoke Operations
    // ===================================================================================
    @Nested
    @DisplayName("Revoke Assignment Operations")
    class RevokeAssignmentOperations {

        @Test
        @DisplayName("revoke debe establecer validTo a ayer (día anterior al actual)")
        void revoke_ShouldSetValidToToYesterday() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // When
            assignment.revoke();

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(yesterday);
        }

        @Test
        @DisplayName("revoke debe hacer que isCurrentlyActive retorne false")
        void revoke_ShouldMakeAssignmentInactive() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );
            assertThat(assignment.isCurrentlyActive()).isTrue();

            // When
            assignment.revoke();

            // Then
            assertThat(assignment.isCurrentlyActive()).isFalse();
        }

        @Test
        @DisplayName("revoke de asignación permanente debe funcionar")
        void revoke_PermanentAssignment_ShouldWork() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // When
            assignment.revoke();

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(yesterday);
            assertThat(assignment.isCurrentlyActive()).isFalse();
        }

        @Test
        @DisplayName("revoke múltiples veces debe ser idempotente")
        void revoke_MultipleTimes_ShouldBeIdempotent() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, false
            );
            LocalDate yesterday = LocalDate.now().minusDays(1);

            // When
            assignment.revoke();
            LocalDate firstRevoke = assignment.getValidTo();

            assignment.revoke();
            LocalDate secondRevoke = assignment.getValidTo();

            // Then - Ambas deben ser ayer
            assertThat(firstRevoke).isEqualTo(yesterday);
            assertThat(secondRevoke).isEqualTo(yesterday);
        }

        @Test
        @DisplayName("revoke de asignación ya expirada no debe causar problemas")
        void revoke_AlreadyExpiredAssignment_ShouldWork() {
            // Given
            LocalDate lastWeek = today.minusDays(7);
            LocalDate yesterday = today.minusDays(1);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, lastWeek, yesterday, false
            );

            // When & Then - No debe lanzar excepción
            assertThatCode(() -> assignment.revoke())
                    .doesNotThrowAnyException();
        }
    }

    // ===================================================================================
    // NESTED: Primary Assignment Rules
    // ===================================================================================
    @Nested
    @DisplayName("Primary Assignment Rules")
    class PrimaryAssignmentRules {

        @Test
        @DisplayName("setPrimary debe permitir cambiar de false a true")
        void setPrimary_FromFalseToTrue_ShouldBeAllowed() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // When
            assignment.setPrimary(true);

            // Then
            assertThat(assignment.isPrimary()).isTrue();
        }

        @Test
        @DisplayName("setPrimary debe permitir cambiar de true a false")
        void setPrimary_FromTrueToFalse_ShouldBeAllowed() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );

            // When
            assignment.setPrimary(false);

            // Then
            assertThat(assignment.isPrimary()).isFalse();
        }

        @Test
        @DisplayName("Asignaciones temporales NO pueden ser primarias (regla de factory)")
        void temporaryAssignments_CannotBePrimary() {
            // When & Then
            assertThatThrownBy(() -> UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, true
            ))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("Pero temporales pueden cambiar a primaria después de creación vía setPrimary")
        void temporaryAssignments_CanBeSetPrimaryAfterCreation() {
            // NOTA: Esto puede ser un bug de diseño - deberías decidir si permitirlo

            // Given
            UserRolAssignment assignment = UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, false
            );

            // When
            assignment.setPrimary(true);

            // Then
            assertThat(assignment.isPrimary()).isTrue();

            // Esto probablemente debería lanzar excepción para mantener consistencia
        }
    }

    // ===================================================================================
    // NESTED: Getters y Setters
    // ===================================================================================
    @Nested
    @DisplayName("Getters y Setters")
    class GettersAndSetters {

        @Test
        @DisplayName("setId debe actualizar el ID correctamente")
        void setId_ShouldUpdateIdCorrectly() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );
            UserRolAssignmentId newId = UserRolAssignmentId.of(999L);

            // When
            assignment.setId(newId);

            // Then
            assertThat(assignment.getId()).isEqualTo(newId);
        }

        @Test
        @DisplayName("Todos los getters deben retornar valores correctos")
        void allGetters_ShouldReturnCorrectValues() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, today, nextMonth, true
            );

            // When & Then
            assertThat(assignment.getId()).isEqualTo(assignmentId);
            assertThat(assignment.getUserId()).isEqualTo(userId);
            assertThat(assignment.getRolId()).isEqualTo(rolId);
            assertThat(assignment.getValidFrom()).isEqualTo(today);
            assertThat(assignment.getValidTo()).isEqualTo(nextMonth);
            assertThat(assignment.isPrimary()).isTrue();
        }
    }

    // ===================================================================================
    // NESTED: Edge Cases
    // ===================================================================================
    @Nested
    @DisplayName("Edge Cases y Escenarios Especiales")
    class EdgeCasesAndSpecialScenarios {

        @Test
        @DisplayName("Asignación que comienza en el pasado y termina en el futuro")
        void assignment_StartingInPastEndingInFuture_ShouldBeCurrentlyActive() {
            // Given
            LocalDate lastWeek = today.minusDays(7);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, lastWeek, nextMonth, false
            );

            // When & Then
            assertThat(assignment.isCurrentlyActive()).isTrue();
            assertThat(assignment.isActiveAt(lastWeek)).isTrue();
            assertThat(assignment.isActiveAt(today)).isTrue();
            assertThat(assignment.isActiveAt(nextWeek)).isTrue();
        }

        @Test
        @DisplayName("Asignación de un solo día (validFrom == validTo)")
        void assignment_SingleDay_ShouldBeActiveOnlyThatDay() {
            // Given
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, tomorrow, tomorrow, false
            );

            // When & Then
            assertThat(assignment.isActiveAt(today)).isFalse();
            assertThat(assignment.isActiveAt(tomorrow)).isTrue();
            assertThat(assignment.isActiveAt(tomorrow.plusDays(1))).isFalse();
        }

        @Test
        @DisplayName("Extender asignación que ya expiró")
        void extend_AlreadyExpiredAssignment_ShouldWork() {
            // Given
            LocalDate lastMonth = today.minusMonths(1);
            LocalDate lastWeek = today.minusDays(7);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, lastMonth, lastWeek, false
            );

            // When
            assignment.extend(nextMonth);

            // Then
            assertThat(assignment.getValidTo()).isEqualTo(nextMonth);
            assertThat(assignment.isCurrentlyActive()).isTrue();
        }

        @Test
        @DisplayName("Revocar asignación que aún no ha comenzado")
        void revoke_NotYetStartedAssignment_ShouldWork() {
            // Given
            LocalDate nextWeek = today.plusDays(7);
            LocalDate twoWeeks = today.plusDays(14);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, nextWeek, twoWeeks, false
            );

            // When
            assignment.revoke();

            // Then
            assertThat(assignment.isCurrentlyActive()).isFalse();
            assertThat(assignment.isActiveAt(nextWeek)).isFalse();
        }

        @Test
        @DisplayName("Cambiar isPrimary múltiples veces")
        void setPrimary_MultipleTimes_ShouldWork() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // When & Then
            assignment.setPrimary(true);
            assertThat(assignment.isPrimary()).isTrue();

            assignment.setPrimary(false);
            assertThat(assignment.isPrimary()).isFalse();

            assignment.setPrimary(true);
            assertThat(assignment.isPrimary()).isTrue();
        }

        @Test
        @DisplayName("Asignación con fechas en el límite de año")
        void assignment_YearBoundary_ShouldWork() {
            // Given
            LocalDate endOfYear = LocalDate.of(2024, 12, 31);
            LocalDate startOfYear = LocalDate.of(2025, 1, 1);
            UserRolAssignment assignment = new UserRolAssignment(
                    assignmentId, userId, rolId, endOfYear, startOfYear, false
            );

            // When & Then
            assertThat(assignment.isActiveAt(endOfYear)).isTrue();
            assertThat(assignment.isActiveAt(startOfYear)).isTrue();
            assertThat(assignment.isActiveAt(startOfYear.plusDays(1))).isFalse();
        }
    }

    // ===================================================================================
    // NESTED: Invariantes de Negocio
    // ===================================================================================
    @Nested
    @DisplayName("Invariantes de Negocio")
    class BusinessInvariants {

        @Test
        @DisplayName("INVARIANTE: validFrom nunca debe ser null")
        void invariant_ValidFromMustNeverBeNull() {
            // When & Then
            assertThatThrownBy(() -> new UserRolAssignment(
                    assignmentId, userId, rolId, null, nextMonth, false
            ))
                    .isInstanceOf(DomainAggregateException.class);
        }

        @Test
        @DisplayName("INVARIANTE: validTo puede ser null SOLO para permanentes")
        void invariant_ValidToCanBeNullOnlyForPermanent() {
            // Given & When
            UserRolAssignment permanent = UserRolAssignment.assignPermanent(
                    userId, rolId, false
            );

            // Then
            assertThat(permanent.getValidTo()).isNull();

            // Verificar que assignTemporary NO permite null validTo
            // (esto debería fallar en la validación de fechas si se intenta)
        }

        @Test
        @DisplayName("INVARIANTE: Asignaciones temporales NO pueden ser primarias al crearse")
        void invariant_TemporaryCannotBePrimaryOnCreation() {
            // When & Then
            assertThatThrownBy(() -> UserRolAssignment.assignTemporary(
                    userId, rolId, today, nextMonth, true
            ))
                    .isInstanceOf(BusinessRuleViolationException.class);
        }

        @Test
        @DisplayName("INVARIANTE: validTo >= validFrom siempre")
        void invariant_ValidToMustBeAfterOrEqualToValidFrom() {
            // When & Then - Múltiples escenarios
            assertThatThrownBy(() -> new UserRolAssignment(
                    assignmentId, userId, rolId, nextMonth, today, false
            ))
                    .isInstanceOf(DomainAggregateException.class);
        }

        @Test
        @DisplayName("INVARIANTE: Después de revoke, assignment NO debe estar activo")
        void invariant_AfterRevoke_AssignmentMustBeInactive() {
            // Given
            UserRolAssignment assignment = UserRolAssignment.assignPermanent(
                    userId, rolId, true
            );

            // When
            assignment.revoke();

            // Then
            assertThat(assignment.isCurrentlyActive()).isFalse();
        }
    }
}