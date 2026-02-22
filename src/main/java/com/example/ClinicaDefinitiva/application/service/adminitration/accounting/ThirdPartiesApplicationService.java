package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.*;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties.ThirdPartiesReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.thirdParties.ThirdPartiesWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.ThirdPartiesUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class ThirdPartiesApplicationService implements ThirdPartiesUseCase {

    private final ThirdPartiesRepository repository;
    private final ThirdPartiesReadMapper readMapper;
    private final ThirdPartiesWriteMapper writeMapper;
    private final CompanyRepository companyRepository;

    public ThirdPartiesApplicationService(ThirdPartiesRepository repository, ThirdPartiesReadMapper readMapper, ThirdPartiesWriteMapper writeMapper, CompanyRepository companyRepository) {
        this.repository = repository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.companyRepository = companyRepository;
    }


    @Override
    public ReadThirdPartyDto findById(ThirdPartiesId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageThirdPartyDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageThirdPartyDto> findByType(String type, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadThirdPartyDto findByDocumentNumber(String documentNumber, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageThirdPartyDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadThirdPartyDto createThirdParty(CreateThirdPartyDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadThirdPartyDto updateContactInformation(ThirdPartiesId id, UpdateThirdPartyDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void activate(ThirdPartiesId id, UserIdentityId requesterId, RolId requesterRolId) {

    }

    @Override
    public void inactivate(ThirdPartiesId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {

    }
}
