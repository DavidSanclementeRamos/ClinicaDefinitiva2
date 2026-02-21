package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.OpeningBalanceUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class OpeningBalanceApplicationService implements OpeningBalanceUseCase {

    @Override
    public ReadOpeningBalanceDto findById(OpeningBalanceId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageOpeningBalanceDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageOpeningBalanceDto> findByAccount(LedgerAccountId accountId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageOpeningBalanceDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadOpeningBalanceDto createOpeningBalance(CreateOpeningBalanceDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void delete(OpeningBalanceId id, UserIdentityId requesterId, RolId requesterRolId) {

    }
}
