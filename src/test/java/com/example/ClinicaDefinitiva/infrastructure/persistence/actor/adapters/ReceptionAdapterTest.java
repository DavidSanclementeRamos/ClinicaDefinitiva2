package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import com.example.ClinicaDefinitiva.domain.actor.output.ReceptionRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.integrationtests.ClinicaDefinitivaIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.data.domain.Pageable;

@ClinicaDefinitivaIntegrationTest
class ReceptionAdapterTest{

    @Autowired
    private ReceptionRepository receptionRepository;
    
    @Autowired
    private UserIdentityRepository userIdentityRepository;

    private Receptionist savedReceptionist;

    @BeforeEach
    void setUp() {
        receptionRepository.findAll(Pageable.unpaged()).forEach(r -> receptionRepository.deleteById(r.getId()));
        userIdentityRepository.findAll(Pageable.unpaged()).forEach(u -> userIdentityRepository.deleteById(u.getId()));

        UserIdentity userIdentity = createIdentity();
        userIdentity = userIdentityRepository.save(userIdentity);
        Person person = createPerson();
        Sector sector = Sector.of(Sector.Type.RECEPTION);
        Receptionist receptionist = Receptionist.registerReceptionist(person, userIdentity.getId(), sector);
        savedReceptionist = receptionRepository.save(receptionist);
    }

    private UserIdentity createIdentity() {
        return UserIdentity.register(
                Email.of("test@test.com").getValue().get(),
                HashedPassword.of("testHashedPassword"),
                UserIdentityName.of("testUser"),
                Instant.parse("2007-12-03T10:15:30.00Z")
        );
    }

    private Person createPerson() {
        Address address = Address.of("Calle 123", "Bogotá", "Cundinamarca", "Colombia", "110111");
        PhoneNumber phone = PhoneNumber.of("3001234567");
        BloodType blood = BloodType.fromLabel("O+");
        DateOfBirth dob = DateOfBirth.of(LocalDate.now().minusYears(30));
        Age age = Age.of(dob);
        Document dni = Document.of("12345678");
        FullName name = FullName.of("Luisa", "Gómez");
        return Person.of(address, age, blood, dob, dni, "EPS123", name, phone);
    }

    @Test
    @DisplayName("Guardar y encontrar recepcionista por ID")
    void saveAndFindById() {
        Optional<Receptionist> found = receptionRepository.findById(savedReceptionist.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedReceptionist.getId());
    }

    @Test
    @DisplayName("Buscar recepcionista por userId")
    void findByUserId() {
        Optional<Receptionist> found = receptionRepository.findByUserId(savedReceptionist.getUserIdentityId());
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(savedReceptionist.getId());
    }

    @Test
    @DisplayName("Actualizar datos de contacto")
    void updateContactData() {
        Receptionist receptionist = receptionRepository.findById(savedReceptionist.getId()).get();
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");
        receptionist.updateContactData(Optional.of(newAddress), Optional.of( newPhone));
        Receptionist updated = receptionRepository.save(receptionist);

        Optional<Receptionist> found = receptionRepository.findById(updated.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(found.get().getPerson().getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    @DisplayName("Buscar por sector")
    void findBySector() {
        var result = receptionRepository.findBySector("RECEPTION", Pageable.unpaged());
        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("Eliminar recepcionista")
    void deleteById() {
        receptionRepository.deleteById(savedReceptionist.getId());
        Optional<Receptionist> found = receptionRepository.findById(savedReceptionist.getId());
        assertThat(found).isEmpty();
    }
}
