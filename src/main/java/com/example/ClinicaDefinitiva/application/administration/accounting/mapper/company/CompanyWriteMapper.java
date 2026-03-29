package com.example.ClinicaDefinitiva.application.administration.accounting.mapper.company;

import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.CreateCompanyDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyContactDto;
import com.example.ClinicaDefinitiva.application.administration.accounting.dto.company.UpdateCompanyTaxDto;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypePerson;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Nit;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class CompanyWriteMapper {

    public Name toName(CreateCompanyDto dto) {
        return Name.of(dto.name());
    }

    public Nit toNit(CreateCompanyDto dto) {
        return Nit.of(dto.taxIdentificationNumber());
    }

    public TypePerson toTypePerson(CreateCompanyDto dto) {
        return TypePerson.valueOf(dto.typePerson());
    }

    public TaxRegime toTaxRegime(CreateCompanyDto dto) {
        return TaxRegime.valueOf(dto.taxRegime());
    }

    public String toLegalRepresentative(CreateCompanyDto dto) {
        return dto.legalRepresentative();
    }

    public Address toAddress(CreateCompanyDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(CreateCompanyDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }

    public Email toEmail(CreateCompanyDto dto) {
        return Email.ofOrThrow(dto.email());
    }


    
    public Name toName(UpdateCompanyContactDto dto) {
        return Name.of(dto.name());
    }

    public Address toAddress(UpdateCompanyContactDto dto) {
        return Address.of(dto.street(), dto.city(), dto.state(), dto.country(), dto.postalCode());
    }

    public PhoneNumber toPhoneNumber(UpdateCompanyContactDto dto) {
        return PhoneNumber.of(dto.phoneNumber());
    }

    public Email toEmail(UpdateCompanyContactDto dto) {
        return Email.ofOrThrow(dto.email()); // uso del nuevo método
    }

    public Nit toNit(UpdateCompanyTaxDto dto) {
        return Nit.of(dto.taxIdentificationNumber());
    }

    public TaxRegime toTaxRegime(UpdateCompanyTaxDto dto) {
        return TaxRegime.valueOf(dto.taxRegime());
    }

    public TypePerson toTypePerson(UpdateCompanyTaxDto dto) {
        return TypePerson.valueOf(dto.typePerson());
    }

    public LocalDate toIncorporationDate(UpdateCompanyTaxDto dto) {
        return dto.incorporationDate();
    }


    
}
