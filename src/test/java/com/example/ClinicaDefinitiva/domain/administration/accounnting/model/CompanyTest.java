
package com.example.ClinicaDefinitiva.domain.administration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;
import com.example.ClinicaDefinitiva.domain.exceptions.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
void shouldRegisterCompanyWithDefaults() {
    Company company = Company.registerCompany(
            Name.of("Clinica OdontoSalud"),
            Nit.of("123456789"),
            TypePerson.JURIDICAL,
            TaxRegime.ORDINARIO,
            "Juan Perez",
             Address.of("Calle 123","Cali","Xd","Colombia","43355"),
             PhoneNumber.of("3001234567"),
             Email.of("info@clinica.com").getValue().get()
    );

    assertEquals("Clinica OdontoSalud", company.getName().getValue());
    assertEquals("123456789", company.getTaxIdentificationNumber().getValue());
    assertEquals(CompanyStatus.of(CompanyStatus.Status.ACTIVE), company.getStatus());
    assertNotNull(company.getIncorporationDate());
}


    @Test
    void shouldThrowExceptionWhenTypePersonIsNull() {
        assertThrows(DomainAggregateException.class, () ->
            Company.builder()
                .withName(Name.of("Clinica OdontoSalud"))
                .withTaxIdentificationNumber(Nit.of("123456789"))
                .withTaxRegime(TaxRegime.ORDINARIO)
                .withIncorporationDate(LocalDate.of(2000, 1, 1))
                .build()
        );
    }

    @Test
    void shouldThrowExceptionWhenIncorporationDateIsFuture() {
        assertThrows(TemporalValidationException.class, () ->
            Company.builder()
                .withName(Name.of("Clinica OdontoSalud"))
                .withTaxIdentificationNumber(Nit.of("123456789"))
                .withTypePerson(TypePerson.JURIDICAL)
                .withTaxRegime(TaxRegime.ORDINARIO)
                .withIncorporationDate(LocalDate.now().plusDays(10))
                .build()
        );
    }

    @Test
    void shouldThrowExceptionWhenIncorporationDateIsTooOld() {
        assertThrows(TemporalValidationException.class, () ->
            Company.builder()
                .withName(Name.of("Clinica OdontoSalud"))
                .withTaxIdentificationNumber(Nit.of("123456789"))
                .withTypePerson(TypePerson.JURIDICAL)
                .withTaxRegime(TaxRegime.ORDINARIO)
                .withIncorporationDate(LocalDate.of(1700, 1, 1))
                .build()
        );
    }

    @Test
    void shouldUpdateContactInformation() {
        Company company = Company.builder()
                .withName(Name.of("Clinica OdontoSalud"))
                .withTaxIdentificationNumber(Nit.of("123456789"))
                .withTypePerson(TypePerson.JURIDICAL)
                .withTaxRegime(TaxRegime.ORDINARIO)
                .withIncorporationDate(LocalDate.of(2000, 1, 1))
                .build();

        company.updateContactInformation(Name.of("Clinica Dental Integral"), "Maria Lopez",
                 Address.of("cr 25","Cali","xd","comlobia","34554"),  PhoneNumber.of("3019876543"),  Email.of("contacto@clinica.com").getValue().get());

        assertEquals("Clinica Dental Integral", company.getName().getValue());
        assertEquals("Maria Lopez", company.getLegalRepresentative());
        assertEquals("contacto@clinica.com", company.getEmail().value());
    }
    
}

    
