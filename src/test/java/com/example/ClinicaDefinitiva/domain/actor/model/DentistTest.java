package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import java.time.DayOfWeek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class DentistTest {

    private Person validPerson;
    private Specialties specialties;
    private UserIdentityId userId;
    private WorkingHours workingHours;

    @BeforeEach
    void setUp() {
        // Crear una persona con edad 30 años
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");

        validPerson = Person.of(address, age, blood, dob, dni, "EPS123", name, phone);

        specialties = Specialties.of(Set.of(Specialty.GENERAL_DENTISTRY));
        userId = UserIdentityId.from(1L);
        workingHours = WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40);
    }

    @Test
    @DisplayName("Registrar dentista con edad válida")
    void shouldRegisterDentist() {
        Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);
        assertThat(dentist).isNotNull();
        assertThat(dentist.getPersonData()).isEqualTo(validPerson);
        assertThat(dentist.getSpecialties()).isEqualTo(specialties);
        assertThat(dentist.getUserId()).isEqualTo(userId);
        assertThat(dentist.getWorkingHours()).isEqualTo(workingHours);
        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE));
        assertThat(dentist.getLastUpdate()).isNotNull();
    }

   @Test
@DisplayName("Registrar dentista con edad inválida lanza excepción")
void shouldThrowWhenAgeInvalid() {
    // Datos fijos
    Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
    PhoneNumber phone = PhoneNumber.of("3001234567");
    BloodType blood = BloodType.fromLabel("O+");
    Document dni = Document.of("12345678");
    FullName name = FullName.of("Juan", "Pérez");
    String eps = "EPS123";

    // Fecha de nacimiento que da edad 20 años (válida para Age)
    DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(20));
    Age age = Age.of(dob);
    Person underAge = Person.of(address, age, blood, dob, dni, eps, name, phone);

    // Crear especialidades, userId, workingHours (si no existen ya)
    Specialties specialties = Specialties.of(Set.of(Specialty.GENERAL_DENTISTRY));
    UserIdentityId userId = UserIdentityId.from(1L);
    WorkingHours workingHours = WorkingHours.of(
        LocalTime.of(8, 0), LocalTime.of(17, 0), DayOfWeek.MONDAY, 40
    );

    assertThatThrownBy(() -> Dentist.registerDentist(underAge, specialties, userId, workingHours))
        .isInstanceOf(BusinessRuleViolationException.class)
        .hasMessageContaining("El odontólogo debe tener al menos 25 años");
     
}

    @Test
@DisplayName("Actualizar datos de contacto")
void shouldUpdateContactData() {
    Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);
    Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
    PhoneNumber newPhone = PhoneNumber.of("3011234567");

LocalDateTime originalLastUpdate = dentist.getLastUpdate();
    dentist.updateContactData(Optional.of(newAddress),Optional.of( newPhone));

    assertThat(dentist.getPersonData().getAddress()).isEqualTo(newAddress);
    assertThat(dentist.getPersonData().getPhoneNumber()).isEqualTo(newPhone);
    // Permitir que la fecha sea igual si ocurre en el mismo milisegundo
    assertThat(dentist.getLastUpdate()).isAfterOrEqualTo(originalLastUpdate);
}

    @Test
    @DisplayName("Actualizar datos sensibles")
    void shouldUpdateSensitiveData() {
        Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);

        BloodType newBlood = BloodType.fromLabel("A+");
        DateOfBirth newDob = DateOfBirth.of(LocalDate.now().minusYears(35));
        Document newDni = Document.of("87654321");
        FullName newName = FullName.of("Pedro", "Gómez");
        Specialties newSpecialties = Specialties.of(Set.of(Specialty.ORTHODONTICS));
        WorkingHours newHours = WorkingHours.of(LocalTime.of(9, 0), LocalTime.of(18, 0), DayOfWeek.TUESDAY, 35);


            dentist.updateSensitiveData(
        Optional.of(newBlood),
        Optional.of(newDob),
        Optional.of(newDni),
        Optional.of("EPS456"),
        Optional.of(newName),
        Optional.of(newSpecialties),
        Optional.of(newHours)
    );

        
        assertThat(dentist.getPersonData().getBloodType()).isEqualTo(newBlood);
        assertThat(dentist.getPersonData().getDateOfBirth()).isEqualTo(newDob);
        assertThat(dentist.getPersonData().getDni()).isEqualTo(newDni);
        assertThat(dentist.getPersonData().getDocumentoEPS()).isEqualTo("EPS456");
        assertThat(dentist.getPersonData().getFullname()).isEqualTo(newName);
        assertThat(dentist.getSpecialties()).isEqualTo(newSpecialties);
        assertThat(dentist.getWorkingHours()).isEqualTo(newHours);
    }

    @Test
    @DisplayName("Aplicar vacaciones")
    void shouldApplyVacation() {
        Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusDays(10);

        dentist.applyVacation(start, end);

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.VACATION));
        assertThat(dentist.getVacationStart()).isEqualTo(start);
        assertThat(dentist.getVacationEnd()).isEqualTo(end);
        assertThat(dentist.getLastUpdate()).isNotNull();
    }

    @Test
    @DisplayName("Aplicar incapacidad")
    void shouldApplyIncapacity() {
        Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = start.plusDays(3);
        String note = "Fiebre";

        dentist.applyIncapacity(start, end, note);

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.SICK_LEAVE));
        assertThat(dentist.getIncapacityStart()).isEqualTo(start);
        assertThat(dentist.getIncapacityEnd()).isEqualTo(end);
        assertThat(dentist.getIncapacityNote()).isEqualTo(note);
    }

    @Test
    @DisplayName("Volver a disponible")
    void shouldReturnToAvailable() {
        Dentist dentist = Dentist.registerDentist(validPerson, specialties, userId, workingHours);
        dentist.applyVacation(LocalDateTime.now(), LocalDateTime.now().plusDays(5));

        dentist.returnToAvailable();

        assertThat(dentist.getAvailabilityStatus()).isEqualTo(DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.AVAILABLE));
        assertThat(dentist.getVacationStart()).isNull();
        assertThat(dentist.getVacationEnd()).isNull();
        assertThat(dentist.getIncapacityStart()).isNull();
        assertThat(dentist.getIncapacityEnd()).isNull();
        assertThat(dentist.getIncapacityNote()).isNull();
    }
}
