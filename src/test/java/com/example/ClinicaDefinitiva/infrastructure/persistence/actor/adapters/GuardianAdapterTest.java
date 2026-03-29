package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Guardian;
import com.example.ClinicaDefinitiva.domain.actor.output.GuardianRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.data.domain.Pageable;

class GuardianAdapterTest extends RepositoryTestBase {

    @Autowired
    private GuardianRepository guardianRepository;

    private Guardian savedGuardian;

    @BeforeEach
    void setUp() {
        guardianRepository.findAll(Pageable.unpaged()).forEach(g -> guardianRepository.deleteById(g.getGuardianId()));

        Person person = createGuardianPerson();
        UserIdentityId userId = UserIdentityId.from(300L);
        TypeGuardian type = TypeGuardian.fromCode("MAMA");
        Guardian guardian = Guardian.registerGuardian(person, userId, type);
        savedGuardian = guardianRepository.save(guardian);
    }

    private Person createGuardianPerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(40));
        Age age = Age.of(dob);
        Document dni = Document.of("87654321");
        FullName name = FullName.of("Juana", "Pérez");
        return Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
    }

    @Test
    @DisplayName("Guardar y encontrar guardian por ID")
    void saveAndFindById() {
        Optional<Guardian> found = guardianRepository.findById(savedGuardian.getGuardianId());
        assertThat(found).isPresent();
        assertThat(found.get().getGuardianId()).isEqualTo(savedGuardian.getGuardianId());
    }

    @Test
    @DisplayName("Buscar guardian por userId")
    void findByUserId() {
        Optional<Guardian> found = guardianRepository.findByUserId(savedGuardian.getUserId());
        assertThat(found).isPresent();
        assertThat(found.get().getGuardianId()).isEqualTo(savedGuardian.getGuardianId());
    }

    @Test
    @DisplayName("Actualizar datos de contacto")
    void updateContactData() {
        Guardian guardian = guardianRepository.findById(savedGuardian.getGuardianId()).get();
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");
        guardian.updateContactData(newAddress, newPhone);
        Guardian updated = guardianRepository.save(guardian);

        Optional<Guardian> found = guardianRepository.findById(updated.getGuardianId());
        assertThat(found).isPresent();
        assertThat(found.get().getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(found.get().getPerson().getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    @DisplayName("Eliminar guardian")
    void deleteById() {
        guardianRepository.deleteById(savedGuardian.getGuardianId());
        Optional<Guardian> found = guardianRepository.findById(savedGuardian.getGuardianId());
        assertThat(found).isEmpty();
    }
}