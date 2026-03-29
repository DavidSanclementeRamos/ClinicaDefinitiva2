package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.LedgerAccount;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.LedgerAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class LedgerAccountReadEntityMapper {

    public LedgerAccount toDomain(LedgerAccountEntity entity) {
        if (entity == null) return null;

        return LedgerAccount.registerLedgerAccount(
                CompanyId.of(entity.getCompany().getId()),
                entity.getCode(),
                Name.of(entity.getName()),
                Enum.valueOf(
                        com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta.class,
                        entity.getNature()),
                entity.isRequiresThirdParty(),
                entity.isRequiresDocument()
        );
        // Nota: Necesitarás un método reconstruct para establecer ID y active
    }
}
