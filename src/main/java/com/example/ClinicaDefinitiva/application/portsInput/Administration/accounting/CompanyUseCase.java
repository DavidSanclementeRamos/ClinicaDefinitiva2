package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.CreateCompanyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.PageCompanyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.ReadCompanyDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.UpdateCompanyContactDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.company.UpdateCompanyTaxDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyUseCase {

    ReadCompanyDto findById(CompanyId id, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageCompanyDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageCompanyDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageCompanyDto> findByTaxRegime(String regime, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadCompanyDto createCompany(CreateCompanyDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadCompanyDto updateContactInformation(CompanyId id, UpdateCompanyContactDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadCompanyDto updateTaxInformation(CompanyId id, UpdateCompanyTaxDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadCompanyDto updateStatus(CompanyId id, CompanyStatus newStatus, UserIdentityId requesterId, RolId requesterRolId);

    void deactivate(CompanyId id, UserIdentityId requesterId, RolId requesterRolId);
}


