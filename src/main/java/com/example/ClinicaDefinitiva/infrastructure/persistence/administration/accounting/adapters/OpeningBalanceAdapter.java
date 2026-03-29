package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.OpeningBalance;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.OpeningBalanceRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.OpeningBalanceId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.OpeningBalanceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.AccountJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.OpeningBalanceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ThirdPartyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.openingBalance.OpeningBalanceReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.openingBalance.OpeningBalanceWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Transactional
public class OpeningBalanceAdapter implements OpeningBalanceRepository {

    private final OpeningBalanceJpaRepository openingBalanceJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final ThirdPartyJpaRepository thirdPartyJpaRepository;
    private final OpeningBalanceReadEntityMapper readMapper;
    private final OpeningBalanceWriteEntityMapper writeMapper;

    public OpeningBalanceAdapter(OpeningBalanceJpaRepository openingBalanceJpaRepository,
                                 CompanyJpaRepository companyJpaRepository,
                                 AccountJpaRepository accountJpaRepository,
                                 ThirdPartyJpaRepository thirdPartyJpaRepository,
                                 OpeningBalanceReadEntityMapper readMapper,
                                 OpeningBalanceWriteEntityMapper writeMapper) {
        this.openingBalanceJpaRepository = openingBalanceJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
        this.thirdPartyJpaRepository = thirdPartyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public OpeningBalance save(OpeningBalance openingBalance) {
        if (openingBalance == null) return null;

        OpeningBalanceEntity entity = writeMapper.toEntity(openingBalance);

        if (openingBalance.getCompanyId() != null && openingBalance.getCompanyId().value() != null) {
            companyJpaRepository.findById(openingBalance.getCompanyId().value())
                    .ifPresent(entity::setCompany);
        }

        if (openingBalance.getCuentaId() != null && openingBalance.getCuentaId().getValue() != null) {
            accountJpaRepository.findById(openingBalance.getCuentaId().getValue())
                    .ifPresent(entity::setAccount);
        }

        if (openingBalance.getThirdPartiesId() != null && openingBalance.getThirdPartiesId().getValue() != null) {
            thirdPartyJpaRepository.findById(openingBalance.getThirdPartiesId().getValue())
                    .ifPresent(entity::setThirdParty);
        }

        OpeningBalanceEntity savedEntity = openingBalanceJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OpeningBalance> findById(OpeningBalanceId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return openingBalanceJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpeningBalance> findByCompanyId(CompanyId companyId, Pageable pageable) {
        if (companyId == null || companyId.value() == null) {
            return Page.empty();
        }
        return openingBalanceJpaRepository.findByCompanyId(companyId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpeningBalance> findByAccount(LedgerAccountId accountId, Pageable pageable) {
        if (accountId == null || accountId.getValue() == null) {
            return Page.empty();
        }
        return openingBalanceJpaRepository.findByAccountId(accountId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpeningBalance> findByDate(LocalDate date) {
        return openingBalanceJpaRepository.findByDate(date, Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OpeningBalance> findByAccountAndDate(LedgerAccountId accountId, LocalDate date) {
        if (accountId == null || accountId.getValue() == null || date == null) {
            return Optional.empty();
        }
        return openingBalanceJpaRepository.findByAccountAndDate(accountId.getValue(), date)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OpeningBalance> findAll(Pageable pageable) {
        return openingBalanceJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public void deleteById(OpeningBalanceId openingBalanceId) {
        if (openingBalanceId != null && openingBalanceId.getValue() != null) {
            openingBalanceJpaRepository.deleteById(openingBalanceId.getValue());
        }
    }
}