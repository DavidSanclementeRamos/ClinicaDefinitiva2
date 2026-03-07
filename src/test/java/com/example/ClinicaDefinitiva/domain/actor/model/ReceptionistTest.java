
package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Sector;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.*;
import static org.junit.jupiter.api.Assertions.*;

class ReceptionistTest {

    private Person createPerson(LocalDate dobDate) {
        DateOfBirth dob = DateOfBirth.of(dobDate);
        Age age = Age.of(dob);
        BloodType bloodType = BloodType.fromLabel("O+");
        Document dni = Document.of("123456789");
        FullName fullname = FullName.of("Laura", "Martinez");
        Address address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        return Person.of(address, age, bloodType, dob, dni, "EPS123", fullname, phone);
    }

    private Sector createSector() {
        return Sector.of(Sector.Type.RECEPTION);
    }

    @Test
    void shouldRegisterReceptionistWithValidAge() {
        Person person = createPerson(LocalDate.of(1990, 1, 1)); // edad ~36
        UserIdentityId userId = UserIdentityId.from(1L);
        Sector sector = createSector();

        Receptionist receptionist = Receptionist.registerReceptionist(person, userId, sector);

        assertNotNull(receptionist.getPerson());
        assertEquals(userId, receptionist.getUserIdentityId());
        assertEquals(sector, receptionist.getSector());
        assertNull(receptionist.getId());
    }

    @Test
    void shouldThrowExceptionWhenAgeIsTooLow() {
        Person person = createPerson(LocalDate.of(2010, 1, 1)); // edad ~16
        UserIdentityId userId = UserIdentityId.from(1L);
        Sector sector = createSector();

        assertThrows(BusinessRuleViolationException.class,
            () -> Receptionist.registerReceptionist(person, userId, sector));
    }

    @Test
    void shouldUpdateContactData() {
        Receptionist receptionist = Receptionist.registerReceptionist(
                createPerson(LocalDate.of(1990, 1, 1)),
                UserIdentityId.from(1L),
                createSector()
        );

        Address newAddress = Address.of("Av Siempre Viva 742", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber newPhone = PhoneNumber.of("3109876543");

        receptionist.updateContactData(newAddress, newPhone);

        assertEquals(newAddress, receptionist.getPerson().getAddress());
        assertEquals(newPhone, receptionist.getPerson().getPhoneNumber());
    }

    @Test
    void shouldUpdateSensitiveData() {
        Receptionist receptionist = Receptionist.registerReceptionist(
                createPerson(LocalDate.of(1990, 1, 1)),
                UserIdentityId.from(1L),
                createSector()
        );

        Age newAge = Age.of(DateOfBirth.of(LocalDate.of(1995, 1, 1)));
        BloodType newBloodType = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.of(1995, 1, 1));
        Document newDni = Document.of("987654321");
        FullName newName = FullName.of("Maria", "Lopez");
        Sector newSector = Sector.of(Sector.Type.DENTAL_ASSISTANCE);

        receptionist.updateSensitiveData(newAge, newBloodType, newDob, newDni, "EPS456", newName, newSector);

        assertEquals(newName, receptionist.getPerson().getFullname());
        assertEquals(newBloodType, receptionist.getPerson().getBloodType());
        assertEquals(newSector, receptionist.getSector());
    }
}
