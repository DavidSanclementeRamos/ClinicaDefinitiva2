package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PatientTest {

   private Person adultPerson;
    private Person minorPerson; // edad 15 (elegible)
    private UserIdentityId userId;
    private GuardianId guardianId;

    @BeforeEach
    void setUp() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");

        // Adulto (edad 30)
        DateOfBirth dobAdult = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age ageAdult = Age.of(dobAdult);
        adultPerson = Person.of(address, ageAdult, blood, dobAdult, dni, "EPS123", name, phone);

        // Menor elegible (edad 15)
        DateOfBirth dobMinor = DateOfBirth.of(LocalDate.now().minusYears(15));
        Age ageMinor = Age.of(dobMinor);
        minorPerson = Person.of(address, ageMinor, blood, dobMinor, dni, "EPS123", name, phone);

        userId = UserIdentityId.from(1L);
        guardianId = GuardianId.fromLong(100L);
    }

    @Test
    @DisplayName("Registrar paciente adulto sin guardian (válido)")
    void shouldRegisterAdultWithoutGuardian() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        assertThat(patient).isNotNull();
        assertThat(patient.getPerson()).isEqualTo(adultPerson);
        assertThat(patient.getUser()).isEqualTo(userId);
        assertThat(patient.getGuardianId()).isNull();
        assertThat(patient.requiereResponsable()).isFalse();
        assertThat(patient.hasGuardian()).isFalse();
    }

    @Test
    @DisplayName("Registrar paciente menor con guardian (válido)")
    void shouldRegisterMinorWithGuardian() {
        Patient patient = Patient.registerPatient(minorPerson, userId, guardianId);
        assertThat(patient).isNotNull();
        assertThat(patient.getGuardianId()).isEqualTo(guardianId);
        assertThat(patient.requiereResponsable()).isTrue();
        assertThat(patient.hasGuardian()).isTrue();
    }

    @Test
    @DisplayName("Registrar paciente menor sin guardian lanza excepción")
    void shouldThrowWhenMinorWithoutGuardian() {
        assertThatThrownBy(() -> Patient.registerPatient(minorPerson, userId, null))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("Los pacientes menores de edad deben tener un responsable legal vinculado");
    }

    @Test
    @DisplayName("Registrar paciente con edad no elegible (menor de 13) lanza excepción")
    void shouldThrowWhenAgeNotEligible() {
        // Edad 12 años (no elegible)
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(12));
        Age age = Age.of(dob);
        Person young = Person.of(adultPerson.getAddress(), age, adultPerson.getBloodType(),
                dob, adultPerson.getDni(), adultPerson.getDocumentoEPS(),
                adultPerson.getFullname(), adultPerson.getPhoneNumber());

        assertThatThrownBy(() -> Patient.registerPatient(young, userId, null))
                .isInstanceOf(DomainAggregateException.class)
                .hasMessageContaining("La edad del paciente debe estar en el rango válido");
    }


    @Test
    @DisplayName("Actualizar datos de contacto")
    void shouldUpdateContact() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");

        patient.updatePatientContact(newAddress, newPhone);

        assertThat(patient.getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(patient.getPerson().getPhoneNumber()).isEqualTo(newPhone);
        assertThat(patient.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Actualizar datos sensibles")
    void shouldUpdateSensitiveData() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        BloodType newBlood = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.now().minusYears(35));
        Document newDni = Document.of("87654321");
        FullName newName = FullName.of("María", "Gómez");

        patient.updateSensitiveData(newBlood, newDob, newDni, "EPS456", newName);

        assertThat(patient.getPerson().getBloodType()).isEqualTo(newBlood);
        assertThat(patient.getPerson().getDateOfBirth()).isEqualTo(newDob);
        assertThat(patient.getPerson().getDni()).isEqualTo(newDni);
        assertThat(patient.getPerson().getDocumentoEPS()).isEqualTo("EPS456");
        assertThat(patient.getPerson().getFullname()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Asignar contrato")
    void shouldAssignContract() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        ContractId contractId = ContractId.of(1L);
        patient.assignContract(contractId);
        assertThat(patient.getContractId()).isEqualTo(contractId);
    }

    @Test
    @DisplayName("Remover contrato")
    void shouldRemoveContract() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        patient.assignContract(ContractId.of(1L));
        patient.removeContract();
        assertThat(patient.getContractId()).isNull();
    }

    @Test
    @DisplayName("validateDeactivation retorna fallo si hay tratamientos activos")
    void testValidateDeactivationWithTreatments() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        // Simular tratamientos (usando reflexión o construir con reconstruct)
        // Como no tenemos acceso fácil, podemos usar reconstruct para probar.
        Patient patientWithTreatments = Patient.reconstruct(
                patient.getPatientId(),
                patient.getUser(),
                patient.getGuardianId(),
                patient.getLastUpdate(),
                patient.getContractId(),
                List.of(TreatmentId.of(1L)), // tratamientos
                patient.getPerson()
        );
        assertThat(patientWithTreatments.validateDeactivation().isFailure()).isTrue();
    }

    @Test
    @DisplayName("validateDeactivation retorna ok si no hay tratamientos")
    void testValidateDeactivationOk() {
        Patient patient = Patient.registerPatient(adultPerson, userId, null);
        assertThat(patient.validateDeactivation().isSuccess()).isTrue();
    }
}
