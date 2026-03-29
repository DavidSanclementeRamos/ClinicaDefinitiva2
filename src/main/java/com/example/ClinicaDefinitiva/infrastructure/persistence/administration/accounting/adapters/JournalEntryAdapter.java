
package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;


import com.example.ClinicaDefinitiva.domain.administration.accounting.model.JournalEntry;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.JournalEntryEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.JournalEntryJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.journalEntry.JournalEntryReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.journalEntry.JournalEntryWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageImpl;

@Component
@Transactional
public class JournalEntryAdapter implements JournalEntryRepository {

    private final JournalEntryJpaRepository entryJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final JournalEntryReadEntityMapper readMapper;
    private final JournalEntryWriteEntityMapper writeMapper;

    public JournalEntryAdapter(JournalEntryJpaRepository entryJpaRepository,
                                  CompanyJpaRepository companyJpaRepository,
                                  JournalEntryReadEntityMapper readMapper,
                                  JournalEntryWriteEntityMapper writeMapper) {
        this.entryJpaRepository = entryJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public JournalEntry save(JournalEntry journalEntry) {
        if (journalEntry == null) return null;

        JournalEntryEntity entity = writeMapper.toEntity(journalEntry);

        if (journalEntry.getCompanyId() != null && journalEntry.getCompanyId().value() != null) {
            companyJpaRepository.findById(journalEntry.getCompanyId().value())
                    .ifPresent(entity::setCompany);
        }

        JournalEntryEntity savedEntity = entryJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JournalEntry> findById(JournalEntryId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return entryJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findByCompanyId(CompanyId companyId, Pageable pageable) {
        if (companyId == null || companyId.value() == null) {
            return Page.empty();
        }
        return entryJpaRepository.findByCompanyId(companyId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return entryJpaRepository.findByDateRange(startDate, endDate, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findByAccount(LedgerAccountId accountId, Pageable pageable) {
        if (accountId == null || accountId.getValue() == null) {
            return Page.empty();
        }
        return entryJpaRepository.findByAccountId(accountId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findByThirdParty(ThirdPartiesId thirdPartiesId, Pageable pageable) {
        if (thirdPartiesId == null || thirdPartiesId.getValue() == null) {
            return Page.empty();
        }
        return entryJpaRepository.findByThirdPartyId(thirdPartiesId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findUnpostedEntries() {
        return entryJpaRepository.findUnpostedEntries(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

@Override
@Transactional(readOnly = true)
public Page<JournalEntry> findByDocumentNumber(String documentNumber) {
    Optional<JournalEntryEntity> optional = entryJpaRepository.findByDocumentNumber(documentNumber);
    
    if (optional.isPresent()) {
        JournalEntry entry = readMapper.toDomain(optional.get());
        List<JournalEntry> list = Collections.singletonList(entry);
        return new PageImpl<>(list, Pageable.unpaged(), 1);
    }
    
    return new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);
}

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return entryJpaRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JournalEntry> findAll(Pageable pageable) {
        return entryJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }
}