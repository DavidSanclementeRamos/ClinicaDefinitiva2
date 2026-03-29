package com.example.ClinicaDefinitiva.domain.actor.vo;

import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

class PersonTest {

    @Test
    @DisplayName("Crear Person completa")
    void shouldCreatePerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        Age age = Age.of(DateOfBirth.of(LocalDate.of(1990, 1, 1)));
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.of(1990, 1, 1));
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");

        Person person = Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
        assertThat(person.getDni()).isEqualTo(dni);
        assertThat(person.getFullname()).isEqualTo(name);
        assertThat(person.getPhoneNumber()).isEqualTo(phone);
        assertThat(person.getAddress()).isEqualTo(address);
        assertThat(person.getDateOfBirth()).isEqualTo(dob);
        assertThat(person.getBloodType()).isEqualTo(blood);
        assertThat(person.getAge()).isEqualTo(age);
        assertThat(person.getDocumentoEPS()).isEqualTo("EPS123");
    }

    @Test
    @DisplayName("withSensitiveData actualiza datos sensibles")
    void testWithSensitiveData() {
        Person original = Person.of(null, null, null, null, null, null, null, null);
        BloodType newBlood = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.of(2000, 1, 1));
        Document newDni = Document.of("87654321");
        FullName newName = FullName.of("María", "Gómez");

        Person updated = original.withSensitiveData(newBlood, newDob, newDni, "EPS456", newName);

        assertThat(updated.getBloodType()).isEqualTo(newBlood);
        assertThat(updated.getDateOfBirth()).isEqualTo(newDob);
        assertThat(updated.getDni()).isEqualTo(newDni);
        assertThat(updated.getDocumentoEPS()).isEqualTo("EPS456");
        assertThat(updated.getFullname()).isEqualTo(newName);
        // El resto de campos se conservan nulos
        assertThat(updated.getAddress()).isNull();
        assertThat(updated.getPhoneNumber()).isNull();
        assertThat(updated.getAge()).isNull();     }
}
