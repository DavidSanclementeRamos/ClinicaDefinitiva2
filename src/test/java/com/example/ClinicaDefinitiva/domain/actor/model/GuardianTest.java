
package com.example.ClinicaDefinitiva.domain.actor.model;


import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.TypeGuardian;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GuardianTest {

    private Person createValidPerson(int yearOfBirth) {
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(yearOfBirth, 1, 1));
        Age age = Age.of(dob);
        BloodType bloodType = BloodType.fromLabel("O+");
        Document dni = Document.of("123456789");
        FullName fullname = FullName.of("Carlos", "Perez");
        Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        return Person.of(address, age, bloodType, dob, dni, "EPS123", fullname, phone);
    }

    @Test
    void shouldRegisterValidGuardian() {
        Person person = createValidPerson(1980); // edad ~46
        UserIdentityId userId = UserIdentityId.from(1L);
        TypeGuardian typeGuardian = TypeGuardian.PAPA;

        Guardian guardian = Guardian.registerGuardian(person, userId, typeGuardian);

        assertNotNull(guardian.getPerson());
        assertEquals(TypeGuardian.PAPA, guardian.getTypeGuardian());
        assertEquals(userId, guardian.getUserId());
    }

    @Test
    void shouldThrowExceptionWhenAgeIsInvalid() {
        Person person = createValidPerson(2008); // edad ~18
        UserIdentityId userId = UserIdentityId.from(1L);
        TypeGuardian typeGuardian = TypeGuardian.MAMA;

        assertThrows(BusinessRuleViolationException.class,
            () -> Guardian.registerGuardian(person, userId, typeGuardian));
    }

    @Test
    void shouldUpdateContactData() {
        Guardian guardian = Guardian.registerGuardian(createValidPerson(1980),
                UserIdentityId.from(1L), TypeGuardian.PAPA);

        Address newAddress = Address.of("Av Siempre Viva 742", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber newPhone = PhoneNumber.of("3109876543");

        guardian.updateContactData(newAddress, newPhone);

        assertEquals(newAddress, guardian.getPerson().getAddress());
        assertEquals(newPhone, guardian.getPerson().getPhoneNumber());
    }

    @Test
    void shouldUpdateSensitiveDataWithValidAge() {
        Guardian guardian = Guardian.registerGuardian(createValidPerson(1980),
                UserIdentityId.from(1L), TypeGuardian.PAPA);

        Age newAge = Age.of(DateOfBirth.of(LocalDate.of(1985, 1, 1)));
        BloodType newBloodType = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.of(1985, 1, 1));
        Document newDni = Document.of("987654321");
        FullName newName = FullName.of("Juan", "Lopez");

        guardian.updateSensitiveData(newAge, newBloodType, newDob, newDni,
                "EPS456", newName, TypeGuardian.TIO);

        assertEquals(TypeGuardian.TIO, guardian.getTypeGuardian());
        assertEquals(newName, guardian.getPerson().getFullname());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingSensitiveDataWithInvalidAge() {
        Guardian guardian = Guardian.registerGuardian(createValidPerson(1980),
                UserIdentityId.from(1L), TypeGuardian.PAPA);

        Age invalidAge = Age.of(DateOfBirth.of(LocalDate.of(2010, 1, 1))); // edad ~16
        BloodType bloodType = BloodType.fromLabel("B+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(2010, 1, 1));
        Document dni = Document.of("111222333");
        FullName fullname = FullName.of("Pedro", "Gomez");

        assertThrows(BusinessRuleViolationException.class,
            () -> guardian.updateSensitiveData(invalidAge, bloodType, dob, dni,
                    "EPS789", fullname, TypeGuardian.OTRO));
    }

    @Test
    void shouldFailDeactivationWhenGuardianHasPatients() {
        Guardian guardian = new Guardian(
                GuardianId.fromLong(1L),
                LocalDateTime.now(),
                List.of(PatientId.of(1L)),
                createValidPerson(1980),
                TypeGuardian.PAPA,
                UserIdentityId.from(1L)
        );

        Outcome<Void> result = guardian.validateDeactivation();
        assertTrue(result.isFailure());
    }

    @Test
    void shouldAllowDeactivationWhenGuardianHasNoPatients() {
        Guardian guardian = new Guardian(
                GuardianId.fromLong(1L),
                LocalDateTime.now(),
                List.of(),
                createValidPerson(1980),
                TypeGuardian.PAPA,
                UserIdentityId.from(1L)
        );

        Outcome<Void> result = guardian.validateDeactivation();
        assertTrue(result.isSuccess());
    }
}