
package com.example.ClinicaDefinitiva.domain.actor.model;


import com.example.ClinicaDefinitiva.domain.actor.vo.Age;
import com.example.ClinicaDefinitiva.domain.actor.vo.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.vo.DateOfBirth;
import com.example.ClinicaDefinitiva.domain.actor.vo.Document;
import com.example.ClinicaDefinitiva.domain.actor.vo.FullName;
import com.example.ClinicaDefinitiva.domain.actor.vo.Person;
import com.example.ClinicaDefinitiva.domain.actor.vo.TypeGuardian;


import static org.junit.jupiter.api.Assertions.*;


import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.errorActor.GuardianError;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.util.Outcome;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


@DisplayName("Tests del Agregado Guardian")
class GuardianTest {

    private Person guardianPerson;
    private UserIdentityId userIdentityId;
    private Address address;
    private PhoneNumber phoneNumber;
    private FullName fullName;
    private Document document;
    private DateOfBirth dateOfBirth;
    private Age age;
    private BloodType bloodType;

    @BeforeEach
    void setUp() {
        // Setup de Value Objects comunes
        userIdentityId = UserIdentityId.from(1L);
        address = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

        phoneNumber = PhoneNumber.of("3001234567");
        fullName = FullName.of("María", "González");
        document = Document.of("12345678");
        bloodType = BloodType.fromLabel("O+");

        // Setup Person para guardián (entre 22 y 60 años)
        dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(35));
        age = Age.of(dateOfBirth);
        guardianPerson = Person.of(
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
    @DisplayName("Tests de creación de Guardian")
    class GuardianCreationTests {

        @Test
        @DisplayName("Debe crear un guardián exitosamente")
        void shouldCreateGuardianSuccessfully() {
            // Arrange
            TypeGuardian typeGuardian = TypeGuardian.PAPA;

            // Act
            Guardian guardian = Guardian.registerGuardian(
                guardianPerson,
                userIdentityId,
                typeGuardian
            );

            // Assert
            assertNotNull(guardian);
            assertNull(guardian.getGuardianId());
            assertEquals(guardianPerson, guardian.getPerson());
            assertEquals(userIdentityId, guardian.getUserId());
            assertEquals(typeGuardian, guardian.getTypeGuardian());
            assertNotNull(guardian.getLastUpdate());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolationException cuando la edad del guardián es menor a 22 años")
        void shouldThrowExceptionWhenGuardianAgeIsLessThan22() {
            // Arrange - Guardián de 20 años
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(20));
            age = Age.of(dateOfBirth);
            Person youngGuardian = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber
            );
            
            TypeGuardian typeGuardian = TypeGuardian.MAMA;

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Guardian.registerGuardian(youngGuardian, userIdentityId, typeGuardian)
            );
            
            assertEquals(GuardianError.ERR_RESPONSIBLE_INVALID_AGE, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe lanzar BusinessRuleViolationException cuando la edad del guardián es mayor a 60 años")
        void shouldThrowExceptionWhenGuardianAgeIsGreaterThan60() {
            // Arrange - Guardián de 65 años
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(65));
            age = Age.of(dateOfBirth);
            Person elderlyGuardian = Person.of(
                    address,
                    age,
                    bloodType,
                    dateOfBirth,
                    document,
                    "EPS123",
                    fullName,
                    phoneNumber            );
            
            TypeGuardian typeGuardian = TypeGuardian.ABUELO;

            // Act & Assert
            BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> Guardian.registerGuardian(elderlyGuardian, userIdentityId, typeGuardian)
            );
            
            assertEquals(GuardianError.ERR_RESPONSIBLE_INVALID_AGE, exception.getCatalogo());
        }

        @Test
        @DisplayName("Debe crear guardián con edad exacta en el límite inferior (22 años)")
        void shouldCreateGuardianWithMinimumAge() {
            // Arrange - Edad mínima para guardián (22 años)
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(22));
            age = Age.of(dateOfBirth);
            Person minAgeGuardian = Person.of(
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
            Guardian guardian = Guardian.registerGuardian(
                minAgeGuardian,
                userIdentityId,
                TypeGuardian.HERMANA
            );

            // Assert
            assertNotNull(guardian);
            assertEquals(22, guardian.getPerson().getAge().Value());
        }

        @Test
        @DisplayName("Debe crear guardián con edad exacta en el límite superior (60 años)")
        void shouldCreateGuardianWithMaximumAge() {
            // Arrange - Edad máxima para guardián (60 años)
            dateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(60));
            age = Age.of(dateOfBirth);
            Person maxAgeGuardian = Person.of(
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
            Guardian guardian = Guardian.registerGuardian(
                maxAgeGuardian,
                userIdentityId,
                TypeGuardian.ABUELO
            );

            // Assert
            assertNotNull(guardian);
            assertEquals(60, guardian.getPerson().getAge().Value());
        }
    }

    @Nested
    @DisplayName("Tests de actualización de Guardian")
    class GuardianUpdateTests {

        private Guardian guardian;
        private TypeGuardian typeGuardian;

        @BeforeEach
        void setUp() {
            typeGuardian = TypeGuardian.PAPA;
            guardian = Guardian.registerGuardian(
                guardianPerson,
                userIdentityId,
                typeGuardian
            );
        }

        @Test
        @DisplayName("Debe actualizar datos de contacto del guardián")
        void shouldUpdateGuardianContactData() {
            // Arrange
            Address newAddress = Address.of("Calle 123", "Cali", "Valle", "Colombia", "760001");

            PhoneNumber newPhone = PhoneNumber.of("3151234567");

            LocalDateTime lastUpdateBefore = guardian.getLastUpdate();

            // Act
            guardian.updateContactData(newAddress, newPhone);

            // Assert
            assertEquals(newAddress, guardian.getPerson().getAddress());
            assertEquals(newPhone, guardian.getPerson().getPhoneNumber());
            assertTrue(guardian.getLastUpdate().isAfter(lastUpdateBefore));
        }

        @Test
        @DisplayName("Debe actualizar datos sensibles del guardián correctamente")
        void shouldUpdateGuardianSensitiveData() {
            // Arrange
            BloodType newBloodType = BloodType.fromLabel("B-");
            DateOfBirth newDateOfBirth = DateOfBirth.of(LocalDate.now().minusYears(40));
            Document newDocument = Document.of("11223344");
            String newEps = "EPS Nueva";
            FullName newFullName = FullName.of("Carlos", "Martínez");
            TypeGuardian newTypeGuardian = TypeGuardian.MAMA;

            LocalDateTime lastUpdateBefore = guardian.getLastUpdate();

            // Act
            guardian.updateSensitiveData(
                newBloodType,
                newDateOfBirth,
                newDocument,
                newEps,
                newFullName,
                newTypeGuardian
            );

            // Assert
            Person updatedPerson = guardian.getPerson();
            assertEquals(newBloodType, updatedPerson.getBloodType());
            assertEquals(newDateOfBirth, updatedPerson.getDateOfBirth());
            assertEquals(newDocument, updatedPerson.getDni());
            assertEquals(newEps, updatedPerson.getDocumentoEPS());
            assertEquals(newFullName, updatedPerson.getFullname());
            assertEquals(newTypeGuardian, guardian.getTypeGuardian());
            assertTrue(guardian.getLastUpdate().isAfter(lastUpdateBefore));
        }

       

        @Test
        @DisplayName("Debe permitir actualización sin cambios en la fecha de nacimiento")
        void shouldAllowUpdateWhenBirthDateDoesNotChange() {
            // Arrange
            Age sameAge = guardian.getPerson().getAge();
            DateOfBirth sameDateOfBirth = guardian.getPerson().getDateOfBirth();
            BloodType newBloodType = BloodType.fromLabel("A+");

            // Act
            guardian.updateSensitiveData(
                newBloodType,
                sameDateOfBirth,
                document,
                "EPS123",
                fullName,
                typeGuardian
            );

            // Assert
            assertEquals(newBloodType, guardian.getPerson().getBloodType());
        }
    }

    @Nested
    @DisplayName("Tests de validaciones de Guardian")
    class GuardianValidationTests {

        @Test
        @DisplayName("Debe validar desactivación exitosa sin pacientes asignados")
        void shouldValidateDeactivationSuccessfully() {
            // Arrange
            TypeGuardian typeGuardian = TypeGuardian.PAPA;
            Guardian guardian = Guardian.registerGuardian(
                guardianPerson,
                userIdentityId,
                typeGuardian
            );

            // Act
            Outcome<Void> outcome = guardian.validateDeactivation();

            // Assert
            assertTrue(outcome.isSuccess());
            assertTrue(outcome.getDetalles().isEmpty());
        }

        @Test
        @DisplayName("Debe retornar error al validar desactivación con pacientes asignados")
        void shouldReturnErrorWhenValidatingDeactivationWithPatients() {
            // Note: Este test requiere crear un guardian con pacientes en la lista
            // Como no tenemos acceso al constructor, necesitaríamos una forma de 
            // crear un guardian con pacientes para probar este escenario
            
            // Por ahora, este test es conceptual y requeriría un método de fábrica
            // o builder para crear guardianes con pacientes en tests
        }
    }

    @Nested
    @DisplayName("Tests de tipos de guardián")
    class GuardianTypeTests {

        @Test
        @DisplayName("Debe crear guardián con diferentes tipos válidos")
        void shouldCreateGuardianWithDifferentValidTypes() {
            // Arrange & Act & Assert para cada tipo
            TypeGuardian[] tipos = {
                TypeGuardian.PAPA,
                TypeGuardian.MAMA,
                TypeGuardian.TIO,
                TypeGuardian.TIA,
                TypeGuardian.ABUELO,
                TypeGuardian.ABUELA,
                TypeGuardian.HERMANO,
                TypeGuardian.HERMANA,
                TypeGuardian.TUTOR_LEGAL
            };

            for (TypeGuardian tipo : tipos) {
                Guardian guardian = Guardian.registerGuardian(
                    guardianPerson,
                    userIdentityId,
                    tipo
                );
                
                assertNotNull(guardian);
                assertEquals(tipo, guardian.getTypeGuardian());
            }
        }

        @Test
        @DisplayName("Debe actualizar el tipo de guardián correctamente")
        void shouldUpdateGuardianType() {
            // Arrange
            TypeGuardian initialType = TypeGuardian.PAPA;
            Guardian guardian = Guardian.registerGuardian(
                guardianPerson,
                userIdentityId,
                initialType
            );
            
            TypeGuardian newType = TypeGuardian.TUTOR_LEGAL;

            // Act
            guardian.updateSensitiveData(
                guardian.getPerson().getBloodType(),
                guardian.getPerson().getDateOfBirth(),
                guardian.getPerson().getDni(),
                guardian.getPerson().getDocumentoEPS(),
                guardian.getPerson().getFullname(),
                newType
            );

            // Assert
            assertEquals(newType, guardian.getTypeGuardian());
        }
    }
}