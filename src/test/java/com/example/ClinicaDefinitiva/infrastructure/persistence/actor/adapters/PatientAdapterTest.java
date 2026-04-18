package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.adapters;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.output.PatientRepository;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.UserIdentityRepository;
import com.example.ClinicaDefinitiva.domain.authentication.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.authentication.vo.HashedPassword;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityName;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.data.domain.Pageable;

class PatientAdapterTest extends RepositoryTestBase {

    @Autowired
    private PatientRepository patientRepository;
    
    @Autowired
    private UserIdentityRepository userIdentityRepository;
    
    @Autowired
    private ContractRepository contractRepository;

    private Patient savedPatient;

    @BeforeEach
    void setUp() {
        // Limpiar
        patientRepository.findAll(Pageable.unpaged()).forEach(p -> patientRepository.deleteById(p.getPatientId()));
        userIdentityRepository.findAll(Pageable.unpaged()).forEach(u -> userIdentityRepository.deleteById(u.getId()));
        // Crear paciente adulto sin guardian
        Person person = createAdultPerson();
        UserIdentity userIdentity = createIdentity();
        userIdentity = userIdentityRepository.save(userIdentity);
        Patient patient = Patient.registerPatient(person, userIdentity.getId(), null);
        savedPatient = patientRepository.save(patient);
    }

    private UserIdentity createIdentity() {
        return UserIdentity.register(
                Email.of("test@test.com").getValue().get(),
                HashedPassword.of("testHashedPassword"),
                UserIdentityName.of("testUser"),
                Instant.parse("2007-12-03T10:15:30.00Z")
        );
    }

    private Person createAdultPerson() {
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
    @DisplayName("Guardar y encontrar paciente por ID")
    void saveAndFindById() {
        Optional<Patient> found = patientRepository.findById(savedPatient.getPatientId());
        assertThat(found).isPresent();
        assertThat(found.get().getPatientId()).isEqualTo(savedPatient.getPatientId());
    }

    @Test
    @DisplayName("Buscar paciente por userId")
    void findByUserId() {
        Optional<Patient> found = patientRepository.findByUserId(savedPatient.getUser());
        assertThat(found).isPresent();
        assertThat(found.get().getPatientId()).isEqualTo(savedPatient.getPatientId());
    }

    @Test
    @DisplayName("Actualizar datos de contacto")
    void updateContactData() {
        Patient patient = patientRepository.findById(savedPatient.getPatientId()).get();
        Address newAddress = Address.of("Calle Nueva 456", "Medellín", "Antioquia", "Colombia", "050001");
        PhoneNumber newPhone = PhoneNumber.of("3011234567");
        patient.updatePatientContact(Optional.of(newAddress),Optional.of( newPhone));
        Patient updated = patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findById(updated.getPatientId());
        assertThat(found).isPresent();
        assertThat(found.get().getPerson().getAddress()).isEqualTo(newAddress);
        assertThat(found.get().getPerson().getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    @DisplayName("Asignar contrato a paciente")
    void assignContract() {
        Patient patient = patientRepository.findById(savedPatient.getPatientId()).get();
        ContractId contractId = ContractId.of(999L);
        patient.assignContract(contractId);
        patientRepository.save(patient);

        Optional<Patient> found = patientRepository.findById(patient.getPatientId());
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("Eliminar paciente")
    void deleteById() {
        patientRepository.deleteById(savedPatient.getPatientId());
        Optional<Patient> found = patientRepository.findById(savedPatient.getPatientId());
        assertThat(found).isEmpty();
    }
}