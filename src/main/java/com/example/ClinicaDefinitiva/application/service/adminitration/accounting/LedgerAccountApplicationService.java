package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.*;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount.LedgerAccountReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.LedgerAccount.LedgerAccountWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.LedgerAccountUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class LedgerAccountApplicationService implements LedgerAccountUseCase {

    private final LedgerAccountReadMapper readMapper;
    private final LedgerAccountWriteMapper writeMapper;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;

    public LedgerAccountApplicationService(LedgerAccountReadMapper readMapper, LedgerAccountWriteMapper writeMapper, LedgerAccountRepository ledgerAccountRepository, CompanyRepository companyRepository) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ReadLedgerAccountDto findById(LedgerAccountId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageLedgerAccountDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadLedgerAccountDto findByCode(String code, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageLedgerAccountDto> findByNature(String nature, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageLedgerAccountDto> findByLevel(int level, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageLedgerAccountDto> findByAccountType(String type, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadLedgerAccountDto createLedgerAccount(CreateLedgerAccountDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadLedgerAccountDto updateAccountInformation(LedgerAccountId id, UpdateLedgerAccountDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void activate(LedgerAccountId id, UserIdentityId requesterId, RolId requesterRolId) {

    }

    @Override
    public void inactivate(LedgerAccountId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {

    }
}
