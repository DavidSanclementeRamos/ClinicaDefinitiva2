package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.LedgerAccount;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.LedgerAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class LedgerAccountWriteEntityMapper {

    public LedgerAccountEntity toEntity(LedgerAccount account) {
        if (account == null) return null;

        LedgerAccountEntity entity = new LedgerAccountEntity();

        if (account.getId() != null && account.getId().getValue() != null) {
            entity.setId(account.getId().getValue());
        }

        entity.setCode(account.getCode());
        entity.setName(account.getName().getValue());
        entity.setNature(account.getNature().name());
        entity.setRequiresThirdParty(account.isRequiresThirdParty());
        entity.setRequiresDocument(account.isRequiresDocument());
        entity.setActive(account.isActive());

        return entity;
    }
}
