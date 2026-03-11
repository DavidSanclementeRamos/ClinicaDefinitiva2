
package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.model.Patient;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.GuardianId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.PatientError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;



@DisplayName("Tests del Agregado Patient")
class PatientTest {

    private Person adultPerson;
    private Person minorPerson;
    private UserIdentityId userIdentityId;
    private GuardianId guardianId;
    private Address address;
    private PhoneNumber phoneNumber;
    private FullName fullName;
    private Document document;
    private DateOfBirth dateOfBirth;
    private Age age;
    private BloodType bloodType;

   /** @BeforeEach
    void setUp() {
        // Setup de Value Objects comunes
        userIdentityId = UserIdentityId.from(1L);
        guardianId = GuardianId.fromLong(1L);
        address  = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

        phoneNumber = PhoneNumber.of("3001234567");
        fullName = FullName.of("Juan", "Pérez");
        document = Document.of("12345678");
        bloodType = BloodType.fromLabel("O+");

        // Setup Person para adulto (mayor de 18 años)
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(30));
        age = Age.of(dateOfBirth);
        adultPerson = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
           
        );

        // Setup Person para menor de edad (10 años)
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(10));
        age = Age.of(dateOfBirth);
        minorPerson = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
        );
    }

    @Nested
    @DisplayName("Tests de creación de Patient")
    class PatientCreationTests {

        @Test
        @DisplayName("Debe crear un paciente adulto exitosamente")
        void shouldCreateAdultPatientSuccessfully() {
            // Act
            Patient patient = Patient.registerPatient(
                adultPerson,
                userIdentityId,
                null
            );

            // Assert
            assertNotNull(patient);
            assertNull(patient.getPatientId());
            assertEquals(adultPerson, patient.getPerson());
            assertEquals(userIdentityId, patient.getUser());
            assertNull(patient.getGuardianId());
            assertFalse(patient.requiereResponsable());
            assertFalse(patient.hasGuardian());
            assertNotNull(patient.getLastUpdate());
        }

        @Test
        @DisplayName("Debe crear un paciente menor de edad con guardián exitosamente")
        void shouldCreateMinorPatientWithGuardianSuccessfully() {
            // Act
            Patient patient = Patient.registerPatient(
                minorPerson,
                userIdentityId,
                guardianId
            );

            // Assert
            assertNotNull(patient);
            assertEquals(minorPerson, patient.getPerson());
            assertTrue(patient.requiereResponsable());
            assertTrue(patient.hasGuardian());
            assertEquals(guardianId, patient.getGuardianId());
        }

        @Test
        @DisplayName("Debe lanzar DomainAggregateException cuando la edad no es elegible para registro")
        void shouldThrowExceptionWhenAgeNotEligibleForRegistration() {
            // Arrange
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(80));
            age = Age.of(dateOfBirth);
            Person elderlyPerson  = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
            );

            // Act & Assert
            DomainAggregateException exception = assertThrows(
                DomainAggregateException.class,
                () -> Patient.registerPatient(elderlyPerson, userIdentityId, null)
            );
            
            assertEquals(PatientError.ERR_PATIENT_INVALID_AGE, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolationException cuando menor no tiene guardián")
        void shouldThrowExceptionWhenMinorHasNoGuardian() {
            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Patient.registerPatient(minorPerson, userIdentityId, null)
            );
            
            assertEquals(PatientError.ERR_PATIENT_MINOR_REQUIRES_GUARDIAN, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe manejar correctamente la mayoría de edad exacta (18 años)")
        void shouldHandleExactAdultAge() {
            // Arrange - Edad exacta de 18 años
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(18));
            age = Age.of(dateOfBirth);
            Person newAdult = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
            );

            // Act
            Patient adultPatient = Patient.registerPatient(
                newAdult,
                userIdentityId,
                null
            );

            // Assert
            assertNotNull(adultPatient);
            assertFalse(adultPatient.requiereResponsable());
            assertFalse(adultPatient.hasGuardian());
        }
    }

    @Nested
    @DisplayName("Tests de actualización de Patient")
    class PatientUpdateTests {

        private Patient patient;

        @BeforeEach
        void setUp() {
            patient = Patient.registerPatient(
                adultPerson,
                userIdentityId,
                null
            );
        }

        @Test
        @DisplayName("Debe actualizar datos de contacto del paciente")
        void shouldUpdatePatientContactData() {
            // Arrange
            Address newAddress = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");
            PhoneNumber newPhone = PhoneNumber.of("3009876543");

            LocalDateTime lastUpdateBefore = patient.getLastUpdate();

            // Act
            patient.updatePatientContact(newAddress, newPhone);

            // Assert
            assertEquals(newAddress, patient.getPerson().getAddress());
            assertEquals(newPhone, patient.getPerson().getPhoneNumber());
            assertTrue(patient.getLastUpdate().isAfter(lastUpdateBefore));
        }

        @Test
        @DisplayName("Debe actualizar datos sensibles del paciente")
        void shouldUpdatePatientSensitiveData() {
            // Arrange
            BloodType newBloodType = BloodType.fromLabel("A+");
            DateOfBirth newDateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(35));
            Document newDocument = Document.of("87654321");
            String newEps = "EPS Nueva";
            FullName newFullName = FullName.of("Carlos", "Martínez");

            LocalDateTime lastUpdateBefore = patient.getLastUpdate();

            // Act
            patient.updateSensitiveData(
                newBloodType,
                newDateOfBirth,
                newDocument,
                newEps,
                newFullName
            );

            // Assert
            Person updatedPerson = patient.getPerson();
            assertEquals(newBloodType, updatedPerson.getBloodType());
            assertEquals(newDateOfBirth, updatedPerson.getDateOfBirth());
            assertEquals(newDocument, updatedPerson.getDni());
            assertEquals(newEps, updatedPerson.getDocumentoEPS());
            assertEquals(newFullName, updatedPerson.getFullname());
            assertTrue(patient.getLastUpdate().isAfter(lastUpdateBefore));
        }
    }

    @Nested
    @DisplayName("Tests de gestión de contratos")
    class PatientContractTests {

        private Patient patient;
        private ContractId contractId;

        @BeforeEach
        void setUp() {
            patient = Patient.registerPatient(
                adultPerson,
                userIdentityId,
                null
            );
            contractId = ContractId.of(100L);
        }

        @Test
        @DisplayName("Debe asignar contrato correctamente")
        void shouldAssignContract() {
            // Act
            patient.assignContract(contractId);

            // Assert
            assertEquals(contractId, patient.getContractId());
        }

        @Test
        @DisplayName("Debe remover contrato correctamente")
        void shouldRemoveContract() {
            // Arrange
            patient.assignContract(contractId);

            // Act
            patient.removeContract();

            // Assert
            assertNull(patient.getContractId());
        }

        @Test
        @DisplayName("Debe lanzar NullPointerException al asignar contrato nulo")
        void shouldThrowExceptionWhenAssigningNullContract() {
            // Act & Assert
            assertThrows(NullPointerException.class, () -> patient.assignContract(null));
        }
    }

    @Nested
    @DisplayName("Tests de validaciones de Patient")
    class PatientValidationTests {

        @Test
        @DisplayName("Debe validar desactivación exitosa sin tratamientos activos")
        void shouldValidateDeactivationSuccessfully() {
            // Arrange
            Patient patient = Patient.registerPatient(
                adultPerson,
                userIdentityId,
                null
            );

            // Act
            Outcome<Void> outcome = patient.validateDeactivation();

            // Assert
            assertTrue(outcome.isSuccess());
            assertTrue(outcome.getDetalles().isEmpty());
        }

        @Test
        @DisplayName("Debe mantener consistencia en las reglas de negocio")
        void shouldMaintainBusinessRulesConsistency() {
            // Arrange
            Patient minorPatient = Patient.registerPatient(
                minorPerson,
                userIdentityId,
                guardianId
            );

            // Assert
            assertTrue(minorPatient.requiereResponsable());
            assertTrue(minorPatient.hasGuardian());
            
            // Verificar que no se pueda crear un menor sin guardián
            assertThrows(
                BusinessRuleViolationException.class,
                () -> Patient.registerPatient(minorPerson, userIdentityId, null)
            );
        }
    }*/
}