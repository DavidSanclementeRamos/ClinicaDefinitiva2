
package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Person createPerson(LocalDate dobDate) {
        DateOfBirth dob = DateOfBirth.of(dobDate);
        Age age = Age.of(dob);
        BloodType bloodType = BloodType.fromLabel("O+");
        Document dni = Document.of("123456789");
        FullName fullname = FullName.of("Ana", "Gomez");
        Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        return Person.of(address, age, bloodType, dob, dni, "EPS123", fullname, phone);
    }

    @Test
    void shouldRegisterValidAdultPatientWithoutGuardian() {
        Person person = createPerson(LocalDate.of(1990, 1, 1)); // adulto
        UserIdentityId userId = UserIdentityId.from(1L);

        Patient patient = Patient.registerPatient(person, userId, null);

        assertNotNull(patient.getPerson());
        assertEquals(userId, patient.getUser());
        assertNull(patient.getGuardianId());
    }

    @Test
    void shouldThrowExceptionWhenMinorWithoutGuardian() {
        Person minor = createPerson(LocalDate.of(2010, 1, 1)); // menor
        UserIdentityId userId = UserIdentityId.from(1L);

        assertThrows(BusinessRuleViolationException.class,
            () -> Patient.registerPatient(minor, userId, null));
    }

    @Test
    void shouldRegisterMinorWithGuardian() {
        Person minor = createPerson(LocalDate.of(2010, 1, 1));
        UserIdentityId userId = UserIdentityId.from(1L);
        GuardianId guardianId = GuardianId.fromLong(1L);

        Patient patient = Patient.registerPatient(minor, userId, guardianId);

        assertTrue(patient.requiereResponsable());
        assertTrue(patient.hasGuardian());
    }

    @Test
    void shouldUpdateContactData() {
        Patient patient = Patient.registerPatient(createPerson(LocalDate.of(1990, 1, 1)),
                UserIdentityId.from(1L), null);

        Address newAddress = Address.of("Av Siempre Viva 742", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber newPhone = PhoneNumber.of("3109876543");

        patient.updatePatientContact(newAddress, newPhone);

        assertEquals(newAddress, patient.getPerson().getAddress());
        assertEquals(newPhone, patient.getPerson().getPhoneNumber());
    }

    @Test
    void shouldUpdateSensitiveData() {
        Patient patient = Patient.registerPatient(createPerson(LocalDate.of(1990, 1, 1)),
                UserIdentityId.from(1L), null);

        Age newAge = Age.of(DateOfBirth.of(LocalDate.of(1995, 1, 1)));
        BloodType newBloodType = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.of(1995, 1, 1));
        Document newDni = Document.of("987654321");
        FullName newName = FullName.of("Maria", "Lopez");

        patient.updateSensitiveData(newAge, newBloodType, newDob, newDni, "EPS456", newName);

        assertEquals(newName, patient.getPerson().getFullname());
        assertEquals(newBloodType, patient.getPerson().getBloodType());
    }

    @Test
    void shouldAssignAndRemoveContract() {
        Patient patient = Patient.registerPatient(createPerson(LocalDate.of(1990, 1, 1)),
                UserIdentityId.from(1L), null);

        ContractId contractId = ContractId.of(1L);
        patient.assignContract(contractId);

        assertEquals(contractId, patient.getContractId());

        patient.removeContract();
        assertNull(patient.getContractId());
    }

    @Test
    void shouldFailDeactivationWhenHasTreatments() {
        Patient patient = new Patient(
                ContractId.of(1L),
                LocalDateTime.now(),
                GuardianId.fromLong(1L),
                createPerson(LocalDate.of(1990, 1, 1)),
                List.of(TreatmentId.of(1L)),
                UserIdentityId.from(1L),
                PatientId.of(1L)
        );

        Outcome<Void> result = patient.validateDeactivation();
        assertTrue(result.isFailure());
    }

    @Test
    void shouldAllowDeactivationWhenNoTreatments() {
        Patient patient = new Patient(
                ContractId.of(1L),
                LocalDateTime.now(),
                GuardianId.fromLong(1L),
                createPerson(LocalDate.of(1990, 1, 1)),
                List.of(),
                UserIdentityId.from(1L),
                PatientId.of(1L)
        );

        Outcome<Void> result = patient.validateDeactivation();
        assertTrue(result.isSuccess());
    }
}

