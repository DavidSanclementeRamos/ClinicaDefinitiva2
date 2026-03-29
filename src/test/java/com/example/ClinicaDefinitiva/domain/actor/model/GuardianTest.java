package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.DentistError;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.GuardianError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GuardianTest {

    private Person validPerson;
    private UserIdentityId userId;
    private TypeGuardian type;

    @BeforeEach
    void setUp() {
        // Edad 40 años (dentro del rango 22-60)
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(40));
        Age age = Age.of(dob);
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");

        validPerson = Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
        userId = UserIdentityId.from(1L);
        type = TypeGuardian.fromCode("MAMA");
    }

    @Test
    @DisplayName("Registrar guardian con edad válida")
    void shouldRegisterGuardian() {
        Guardian guardian = Guardian.registerGuardian(validPerson, userId, type);
        assertThat(guardian).isNotNull();
        assertThat(guardian.getPerson()).isEqualTo(validPerson);
        assertThat(guardian.getUserId()).isEqualTo(userId);
        assertThat(guardian.getTypeGuardian()).isEqualTo(type);
        assertThat(guardian.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Registrar guardian con edad inválida (menor de 22) lanza excepción")
    void shouldThrowWhenAgeTooYoung() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(2010, 1, 1));
        Age age = Age.of(dob);
        Person young = Person.of(validPerson.getAddress(), age, validPerson.getBloodType(),
                dob, validPerson.getDni(), validPerson.getDocumentoEPS(),
                validPerson.getFullname(), validPerson.getPhoneNumber());

        assertThatThrownBy(() -> Guardian.registerGuardian(young, userId, type))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El responsable debe tener entre 22 y 60 años");
    }

    @Test
    @DisplayName("Registrar guardian con edad inválida (mayor de 60) lanza excepción")
    void shouldThrowWhenAgeTooOld() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(65));
        Age age = Age.of(dob);
        Person old = Person.of(validPerson.getAddress(), age, validPerson.getBloodType(),
                dob, validPerson.getDni(), validPerson.getDocumentoEPS(),
                validPerson.getFullname(), validPerson.getPhoneNumber());

        assertThatThrownBy(() -> Guardian.registerGuardian(old, userId, type))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("El responsable debe tener entre 22 y 60 años");
    }

    @Test
    @DisplayName("Actualizar datos de contacto")
    void shouldUpdateContactData() {
        Guardian guardian = Guardian.registerGuardian(validPerson, userId, type);
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");

        guardian.updateContactData(newAddress, newPhone);

        assertThat(guardian.getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(guardian.getPerson().getPhoneNumber()).isEqualTo(newPhone);
        assertThat(guardian.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Actualizar datos sensibles")
    void shouldUpdateSensitiveData() {
        Guardian guardian = Guardian.registerGuardian(validPerson, userId, type);
        BloodType newBlood = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.now().minusYears(35));
        Document newDni = Document.of("87654321");
        FullName newName = FullName.of("María", "Gómez");
        TypeGuardian newType = TypeGuardian.fromCode("PAPA");

        guardian.updateSensitiveData(newBlood, newDob, newDni, "EPS456", newName, newType);

        assertThat(guardian.getPerson().getBloodType()).isEqualTo(newBlood);
        assertThat(guardian.getPerson().getDateOfBirth()).isEqualTo(newDob);
        assertThat(guardian.getPerson().getDni()).isEqualTo(newDni);
        assertThat(guardian.getPerson().getDocumentoEPS()).isEqualTo("EPS456");
        assertThat(guardian.getPerson().getFullname()).isEqualTo(newName);
        assertThat(guardian.getTypeGuardian()).isEqualTo(newType);
        assertThat(guardian.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("validateDeactivation retorna fallo si tiene pacientes asociados")
    void testValidateDeactivationWithPatients() {
        Guardian guardian = Guardian.registerGuardian(validPerson, userId, type);
        // Usamos reconstruct para agregar lista de pacientes
        Guardian guardianWithPatients = Guardian.reconstruct(
                guardian.getGuardianId(),
                guardian.getPerson(),
                guardian.getTypeGuardian(),
                guardian.getUserId(),
                List.of(PatientId.of(1L)), // pacientes
                guardian.getLastUpdate()
        );
        assertThat(guardianWithPatients.validateDeactivation().isFailure()).isTrue();
    }

    @Test
    @DisplayName("validateDeactivation retorna ok si no tiene pacientes")
    void testValidateDeactivationOk() {
        Guardian guardian = Guardian.registerGuardian(validPerson, userId, type);
        assertThat(guardian.validateDeactivation().isSuccess()).isTrue();
    }
}
