
package com.example.ClinicaDefinitiva.domain.integration;



import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests de Integración Patient-Guardian")
class PatientGuardianIntegrationTest {

    private Person adultPerson;
    private Person minorPerson;
    private Person guardianPerson;
    private UserIdentityId userIdentityId;
    private GuardianId guardianId;
    private Address address;
    private PhoneNumber phoneNumber;
    private FullName fullName;
    private Document document;
    private DateOfBirth dateOfBirth;
    private Age age;
    private BloodType bloodType;

    @BeforeEach
    void setUp() {
        userIdentityId = UserIdentityId.from(1L);
        guardianId = GuardianId.fromLong(1L);
        address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

        phoneNumber = PhoneNumber.of("3001234567");
        fullName = FullName.of("Juan", "Pérez");
        document = Document.of("12345678");
        bloodType = BloodType.fromLabel("O+");

        // Setup Person adulto
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(30));
        age = Age.of(dateOfBirth);
        adultPerson = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
        );

        // Setup Person menor
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(10));
        age = Age.of(dateOfBirth);
        minorPerson = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
        );

        // Setup Person guardián
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(35));
        age = Age.of(dateOfBirth);
        guardianPerson = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
        );
    }

    @Test
    @DisplayName("Debe permitir asociar un paciente menor con un guardián")
    void shouldAssociateMinorPatientWithGuardian() {
        // Arrange
        TypeGuardian typeGuardian = TypeGuardian.MAMA;
        Guardian guardian = Guardian.registerGuardian(
            guardianPerson,
            userIdentityId,
            typeGuardian
        );

        // Act
        Patient minorPatient = Patient.registerPatient(
            minorPerson,
            UserIdentityId.from(2L),
            guardian.getGuardianId() // null en este test porque guardianId no está persistido
        );

        // Assert
        assertNotNull(minorPatient);
        assertTrue(minorPatient.requiereResponsable());
        assertTrue(minorPatient.hasGuardian());
    }

    @Test
    @DisplayName("Debe rechazar un paciente menor sin guardián")
    void shouldRejectMinorPatientWithoutGuardian() {
        // Act & Assert
        assertThrows(
            BusinessRuleViolationException.class,
            () -> Patient.registerPatient(minorPerson, userIdentityId, null)
        );
    }

    @Test
    @DisplayName("Debe aceptar un paciente adulto sin guardián")
    void shouldAcceptAdultPatientWithoutGuardian() {
        // Act
        Patient adultPatient = Patient.registerPatient(
            adultPerson,
            userIdentityId,
            null
        );

        // Assert
        assertNotNull(adultPatient);
        assertFalse(adultPatient.requiereResponsable());
        assertFalse(adultPatient.hasGuardian());
    }

    @Test
    @DisplayName("Debe rechazar un guardián con edad fuera del rango permitido")
    void shouldRejectGuardianWithInvalidAge() {
        // Arrange - Guardián muy joven
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(20));
        age = Age.of(dateOfBirth);
        Person youngGuardian = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
        );

        TypeGuardian typeGuardian = TypeGuardian.PAPA;

        // Act & Assert
        assertThrows(
            BusinessRuleViolationException.class,
            () -> Guardian.registerGuardian(youngGuardian, userIdentityId, typeGuardian)
        );
    }
}
