package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company.CompanyReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.NameMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.NitMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company.CompanyWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.CompanyUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyApplicationService implements CompanyUseCase {

    private final CompanyReadMapper readMapper;
    private final CompanyWriteMapper writeMapper;
    private final CompanyRepository repository;

    public CompanyApplicationService(CompanyReadMapper readMapper, CompanyWriteMapper writeMapper, CompanyRepository repository) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
    }

    @Override
    public ReadCompanyDto findById(CompanyId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageCompanyDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageCompanyDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageCompanyDto> findByTaxRegime(String regime, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadCompanyDto createCompany(CreateCompanyDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadCompanyDto updateContactInformation(CompanyId id, UpdateCompanyContactDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadCompanyDto updateTaxInformation(CompanyId id, UpdateCompanyTaxDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadCompanyDto updateStatus(CompanyId id, String newStatus, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void deactivate(CompanyId id, UserIdentityId requesterId, RolId requesterRolId) {

    }
}