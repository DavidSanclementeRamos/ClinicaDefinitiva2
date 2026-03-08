package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance.CreateOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance.PageOpeningBalanceDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.openingBalance.ReadOpeningBalanceDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OpeningBalanceUseCase {
    ReadOpeningBalanceDto findById(OpeningBalanceId id, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageOpeningBalanceDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageOpeningBalanceDto> findByAccount(LedgerAccountId accountId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageOpeningBalanceDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadOpeningBalanceDto createOpeningBalance(CreateOpeningBalanceDto dto, UserIdentityId requesterId, RolId requesterRolId);

    void delete(OpeningBalanceId id, UserIdentityId requesterId, RolId requesterRolId);


}
