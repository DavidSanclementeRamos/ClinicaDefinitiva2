package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class ReceptionistTest {

    private Person validPerson;
    private UserIdentityId userId;
    private Sector sector;

    @BeforeEach
    void setUp() {
        // Edad 30 años (dentro del rango 20-130)
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");

        validPerson = Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
        userId = UserIdentityId.from(1L);
        sector = Sector.of(Sector.Type.RECEPTION);
    }

    @Test
    @DisplayName("Registrar recepcionista con edad válida")
    void shouldRegisterReceptionist() {
        Receptionist receptionist = Receptionist.registerReceptionist(validPerson, userId, sector);
        assertThat(receptionist).isNotNull();
        assertThat(receptionist.getPerson()).isEqualTo(validPerson);
        assertThat(receptionist.getUserIdentityId()).isEqualTo(userId);
        assertThat(receptionist.getSector()).isEqualTo(sector);
        assertThat(receptionist.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Registrar recepcionista con edad inválida (menor de 20) lanza excepción")
    void shouldThrowWhenAgeTooYoung() {
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(18));
        Age age = Age.of(dob);
        Person young = Person.of(validPerson.getAddress(), age, validPerson.getBloodType(),
                dob, validPerson.getDni(), validPerson.getDocumentoEPS(),
                validPerson.getFullname(), validPerson.getPhoneNumber());

        assertThatThrownBy(() -> Receptionist.registerReceptionist(young, userId, sector))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("La edad del recepcionista es insuficiente para cumplir con los requisitos mínimos");
    }

    @Test
    @DisplayName("Actualizar datos de contacto")
    void shouldUpdateContactData() {
        Receptionist receptionist = Receptionist.registerReceptionist(validPerson, userId, sector);
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");

        receptionist.updateContactData(Optional.of(newAddress),Optional.of( newPhone));

        assertThat(receptionist.getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(receptionist.getPerson().getPhoneNumber()).isEqualTo(newPhone);
        assertThat(receptionist.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Actualizar datos sensibles")
    void shouldUpdateSensitiveData() {
        Receptionist receptionist = Receptionist.registerReceptionist(validPerson, userId, sector);
        BloodType newBlood = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.now().minusYears(35));
        Document newDni = Document.of("87654321");
        FullName newName = FullName.of("María", "Gómez");
        Sector newSector = Sector.of(Sector.Type.BILLING);


            receptionist.updateSensitiveData(
        Optional.of(newBlood),
        Optional.of(newDob),
        Optional.of(newDni),
        Optional.of("EPS456"),
        Optional.of(newName),
        Optional.of(newSector)
    );

        
        assertThat(receptionist.getPerson().getBloodType()).isEqualTo(newBlood);
        assertThat(receptionist.getPerson().getDateOfBirth()).isEqualTo(newDob);
        assertThat(receptionist.getPerson().getDni()).isEqualTo(newDni);
        assertThat(receptionist.getPerson().getDocumentoEPS()).isEqualTo("EPS456");
        assertThat(receptionist.getPerson().getFullname()).isEqualTo(newName);
        assertThat(receptionist.getSector()).isEqualTo(newSector);
        assertThat(receptionist.getLastUpdate()).isNotNull();
    }
}
