package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.data.domain.Pageable;

class DentistAdapterTest extends RepositoryTestBase {

    @Autowired
    private DentistRepository dentistRepository;

    private Dentist savedDentist;

    @BeforeEach
    void setUp() {
        // Limpiar datos previos (si es necesario)
        dentistRepository.findAll(Pageable.unpaged()).forEach(d -> dentistRepository.deleteById(d.getDentistId()));

        // Crear y guardar un dentista de prueba
        Person person = createPerson();
        Specialties specialties = Specialties.of(Set.of(Specialty.GENERAL_DENTISTRY));
        UserIdentityId userId = UserIdentityId.from(100L);
        WorkingHours workingHours = WorkingHours.of(LocalTime.of(8, 0), LocalTime.of(17, 0),
                java.time.DayOfWeek.MONDAY, 40);

        Dentist dentist = Dentist.registerDentist(person, specialties, userId, workingHours);
        savedDentist = dentistRepository.save(dentist);
    }

    private Person createPerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Juan", "Pérez");
        return Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
    }

    @Test
    @DisplayName("Guardar y encontrar dentista por ID")
    void saveAndFindById() {
        Optional<Dentist> found = dentistRepository.findById(savedDentist.getDentistId());
        assertThat(found).isPresent();
        assertThat(found.get().getDentistId()).isEqualTo(savedDentist.getDentistId());
        assertThat(found.get().getPersonData().getFullname()).isEqualTo(savedDentist.getPersonData().getFullname());
    }

    @Test
    @DisplayName("Buscar dentista por userId")
    void findByUserId() {
        UserIdentityId userId = savedDentist.getUserId();
        Optional<Dentist> found = dentistRepository.findByUserId(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getDentistId()).isEqualTo(savedDentist.getDentistId());
    }

    @Test
    @DisplayName("Actualizar datos de contacto y persistir")
    void updateContactData() {
        // Recuperar y modificar
        Dentist dentist = dentistRepository.findById(savedDentist.getDentistId()).get();
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");
        dentist.updateContactData(Optional.of(newAddress), Optional.of(newPhone));

        Dentist updated = dentistRepository.save(dentist);

        // Verificar que se guardó
        Optional<Dentist> found = dentistRepository.findById(updated.getDentistId());
        assertThat(found).isPresent();
        assertThat(found.get().getPersonData().getAddress()).isEqualTo(newAddress);
        assertThat(found.get().getPersonData().getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    @DisplayName("Buscar dentistas por especialidad")
    void findBySpecialty() {
        Pageable pageable = Pageable.unpaged();
        var result = dentistRepository.findBySpecialty("General Dentistry", pageable);
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
        // Si tuvieras otro dentista con otra especialidad, lo validarías
    }

    @Test
    @DisplayName("Eliminar dentista por ID")
    void deleteById() {
        dentistRepository.deleteById(savedDentist.getDentistId());
        Optional<Dentist> found = dentistRepository.findById(savedDentist.getDentistId());
        assertThat(found).isEmpty();
    }
}