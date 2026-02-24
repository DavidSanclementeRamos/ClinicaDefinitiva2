
package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.vo.Specialty;
import com.example.ClinicaDefinitiva.domain.actor.vo.WorkingHours;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class DentistTest {

    private Person createValidPerson() {
           
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 1, 1));
        Age age = Age.of(dob);
        BloodType bloodType = BloodType.fromLabel("O+");
        Document dni = Document.of("123456789");
        FullName fullname = FullName.of("John", "Doe");
        Address address = Address.of("cr 28p ", "Cali","XD","Colombi","236456");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        return Person.of(address, age, bloodType, dob, dni, "EPS123", fullname, phone);
    }

    private Specialties createSpecialties() {
        return Specialties.of(Set.of(Specialty.of("Orthodontics")));
    }

    private WorkingHours createWorkingHours() {
        return WorkingHours.of(LocalTime.of(8,0), LocalTime.of(16,0), DayOfWeek.MONDAY, 40);
    }

    @Test
    void shouldRegisterValidDentist() {
        Person person = createValidPerson();
        Specialties specialties = createSpecialties();
        UserIdentityId userId = UserIdentityId.from(1L);
        WorkingHours wh = createWorkingHours();

        Dentist dentist = Dentist.registerDentist(person, specialties, userId, wh, LocalDateTime.now());

        assertNotNull(dentist.getPersonData());
        assertEquals(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE),
                     dentist.getAvailabilityStatus());
    }

    @Test
void shouldThrowExceptionWhenAgeIsTooLow() {
    DateOfBirth dob = DateOfBirth.of(LocalDate.of(2010, 1, 1)); // edad ~16 en 2026
    Age age = Age.of(dob);
    BloodType bloodType = BloodType.fromLabel("O+");
    Document dni = Document.of("987654321");
    FullName fullname = FullName.of("Jane", "Doe");
    Address address = Address.of("Av Siempre Viva 742", "Cali", "Valle", "Colombia", "760001");
    PhoneNumber phone = PhoneNumber.of("3009876543");
    Person person = Person.of(address, age, bloodType, dob, dni, "EPS456", fullname, phone);

    Specialties specialties = createSpecialties();
    UserIdentityId userId = UserIdentityId.from(1L);
    WorkingHours wh = createWorkingHours();

    assertThrows(BusinessRuleViolationException.class,
        () -> Dentist.registerDentist(person, specialties, userId, wh, LocalDateTime.now()));
}

    @Test
    void shouldApplyVacation() {
        Dentist dentist = Dentist.registerDentist(createValidPerson(), createSpecialties(),
                UserIdentityId.from(1L), createWorkingHours(), LocalDateTime.now());

        LocalDateTime start = LocalDateTime.of(2026, 2, 1, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 10, 18, 0);

        dentist.applyVacation(start, end);

        assertEquals(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION),
                     dentist.getAvailabilityStatus());
    }

    @Test
    void shouldApplyIncapacity() {
        Dentist dentist = Dentist.registerDentist(createValidPerson(), createSpecialties(),
                UserIdentityId.from(1L), createWorkingHours(), LocalDateTime.now());

        LocalDateTime start = LocalDateTime.of(2026, 2, 15, 8, 0);
        LocalDateTime end = LocalDateTime.of(2026, 2, 20, 18, 0);

        dentist.applyIncapacity(start, end, "Flu");

        assertEquals(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE),
                     dentist.getAvailabilityStatus());
    }

    @Test
    void shouldReturnToAvailable() {
        Dentist dentist = Dentist.registerDentist(createValidPerson(), createSpecialties(),
                UserIdentityId.from(1L), createWorkingHours(), LocalDateTime.now());

        dentist.applyVacation(LocalDateTime.now(), LocalDateTime.now().plusDays(5));
        dentist.returnToAvailable();

        assertEquals(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE),
                     dentist.getAvailabilityStatus());
        assertNull(dentist.getIncapacityNote());
    }
}