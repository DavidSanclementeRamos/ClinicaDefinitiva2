package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ThirdPartiesRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ThirdPartyEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ThirdPartyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.thirdParties.ThirdPartyReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.thirdParties.ThirdPartyWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class ThirdPartiesAdapter implements ThirdPartiesRepository {

    private final ThirdPartyJpaRepository thirdPartyJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final ThirdPartyReadEntityMapper readMapper;
    private final ThirdPartyWriteEntityMapper writeMapper;

    public ThirdPartiesAdapter(ThirdPartyJpaRepository thirdPartyJpaRepository,
                             CompanyJpaRepository companyJpaRepository,
                             ThirdPartyReadEntityMapper readMapper,
                             ThirdPartyWriteEntityMapper writeMapper) {
        this.thirdPartyJpaRepository = thirdPartyJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public ThirdParties save(ThirdParties thirdParties) {
        if (thirdParties == null) return null;

        ThirdPartyEntity entity = writeMapper.toEntity(thirdParties);

        if (thirdParties.getCompanyId() != null && thirdParties.getCompanyId().value() != null) {
            companyJpaRepository.findById(thirdParties.getCompanyId().value())
                    .ifPresent(entity::setCompany);
        }

        ThirdPartyEntity savedEntity = thirdPartyJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThirdParties> findById(ThirdPartiesId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return thirdPartyJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ThirdParties> findByDocumentNumber(String documentNumber) {
        return thirdPartyJpaRepository.findByDocumentNumber(documentNumber)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThirdParties> findByCompanyId(CompanyId companyId, Pageable pageable) {
        if (companyId == null || companyId.value() == null) {
            return Page.empty();
        }
        return thirdPartyJpaRepository.findByCompanyId(companyId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThirdParties> findByType(String type, Pageable pageable) {
        return thirdPartyJpaRepository.findByThirdPartyType(type, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThirdParties> findActiveByType(com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties type) {
        return thirdPartyJpaRepository.findActiveByType(type.name(), Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThirdParties> findAll() {
        return thirdPartyJpaRepository.findAll(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocumentNumber(String documentNumber) {
        return thirdPartyJpaRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ThirdParties> findAll(Pageable pageable) {
        return thirdPartyJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }
}