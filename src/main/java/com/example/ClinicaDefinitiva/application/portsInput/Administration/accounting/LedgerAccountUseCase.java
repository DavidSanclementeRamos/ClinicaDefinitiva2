package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LedgerAccountUseCase {
        ReadLedgerAccountDto findById(LedgerAccountId id, UserIdentityId requesterId, RolId requesterRolId);

        Page<PageLedgerAccountDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

        ReadLedgerAccountDto findByCode(String code, UserIdentityId requesterId, RolId requesterRolId);

        Page<PageLedgerAccountDto> findByNature(String nature, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

        Page<PageLedgerAccountDto> findByLevel(int level, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

        Page<PageLedgerAccountDto> findByAccountType(String type, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

        ReadLedgerAccountDto createLedgerAccount(CreateLedgerAccountDto dto, UserIdentityId requesterId, RolId requesterRolId);

        ReadLedgerAccountDto updateAccountInformation(LedgerAccountId id, UpdateLedgerAccountDto dto, UserIdentityId requesterId, RolId requesterRolId);

        void activate(LedgerAccountId id, UserIdentityId requesterId, RolId requesterRolId);

        void inactivate(LedgerAccountId id, String reason, UserIdentityId requesterId, RolId requesterRolId);


}
