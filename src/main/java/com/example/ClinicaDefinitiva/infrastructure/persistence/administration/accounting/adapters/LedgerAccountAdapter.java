
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.LedgerAccountEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.AccountJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.LedgerAccount.LedgerAccountReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.LedgerAccount.LedgerAccountWriteEntityMapper;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional

public class LedgerAccountAdapter implements LedgerAccountRepository {




    private final AccountJpaRepository accountJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final LedgerAccountReadEntityMapper readMapper;
    private final LedgerAccountWriteEntityMapper writeMapper;

    public LedgerAccountAdapter(AccountJpaRepository accountJpaRepository,
                          CompanyJpaRepository companyJpaRepository,
                          LedgerAccountReadEntityMapper readMapper,
                          LedgerAccountWriteEntityMapper writeMapper) {
        this.accountJpaRepository = accountJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public LedgerAccount save(LedgerAccount ledgerAccount) {
        if (ledgerAccount == null) return null;

        LedgerAccountEntity entity = writeMapper.toEntity(ledgerAccount);

        if (ledgerAccount.getCompanyId() != null && ledgerAccount.getCompanyId().value() != null) {
            companyJpaRepository.findById(ledgerAccount.getCompanyId().value())
                    .ifPresent(entity::setCompany);
        }

        LedgerAccountEntity savedEntity = accountJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerAccount> findById(LedgerAccountId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return accountJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LedgerAccount> findByCode(String code) {
        return accountJpaRepository.findByCode(code)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerAccount> findByCompanyId(CompanyId companyId) {
        return accountJpaRepository.findByCompanyId(companyId.value(), Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerAccount> findByNature(String nature, Pageable pageable) {
        return accountJpaRepository.findByNature(nature, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerAccount> findActiveAccounts() {
        return accountJpaRepository.findActiveAccounts(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerAccount> findByLevel(int level, Pageable pageable) {
        return accountJpaRepository.findByLevel(level, pageable)
                .map(readMapper::toDomain);
    }

    @Override
@Transactional(readOnly = true)
public Page<LedgerAccount> findChildAccounts(String parentCode) {
    return accountJpaRepository.findChildAccounts(parentCode, Pageable.unpaged())
            .map(readMapper::toDomain);
}

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return accountJpaRepository.existsByCode(code);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LedgerAccount> findAll(Pageable pageable) {
        return accountJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

  @Override
@Transactional(readOnly = true)
public Page<LedgerAccount> findByAccountType(String type, Pageable pageable) {
    String firstDigit = switch (type.toUpperCase()) {
        case "ASSET" -> "1";
        case "LIABILITY" -> "2";
        case "EQUITY" -> "3";
        case "INCOME" -> "4";
        case "EXPENSE" -> "5";
        case "COST" -> "6";
        case "COST_OF_SALES" -> "7";
        default -> null;
    };
    
    if (firstDigit == null) {
        return Page.empty();
    }
    
    return accountJpaRepository.findByFirstDigit(firstDigit, pageable)
            .map(readMapper::toDomain);
}
}