package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ContractRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.ContractEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ContractJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ThirdPartyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.contract.ContractReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.contract.ContractWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Transactional
public class ContractAdapter implements ContractRepository {

    private final ContractJpaRepository contractJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;
    private final ThirdPartyJpaRepository thirdPartyJpaRepository;
    private final ContractReadEntityMapper readMapper;
    private final ContractWriteEntityMapper writeMapper;

    public ContractAdapter(ContractJpaRepository contractJpaRepository,
                           CompanyJpaRepository companyJpaRepository,
                           ThirdPartyJpaRepository thirdPartyJpaRepository,
                           ContractReadEntityMapper readMapper,
                           ContractWriteEntityMapper writeMapper) {
        this.contractJpaRepository = contractJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
        this.thirdPartyJpaRepository = thirdPartyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public Contract save(Contract contract) {
        if (contract == null) return null;

        ContractEntity entity = writeMapper.toEntity(contract);

        if (contract.getCompanyId() != null && contract.getCompanyId().value() != null) {
            companyJpaRepository.findById(contract.getCompanyId().value())
                    .ifPresent(entity::setCompany);
        }

        if (contract.getThirdPartiesId() != null && contract.getThirdPartiesId().getValue() != null) {
            thirdPartyJpaRepository.findById(contract.getThirdPartiesId().getValue())
                    .ifPresent(entity::setThirdParty);
        }

        ContractEntity savedEntity = contractJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Contract> findById(ContractId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return contractJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findByCompanyId(CompanyId companyId, Pageable pageable) {
        if (companyId == null || companyId.value() == null) {
            return Page.empty();
        }
        return contractJpaRepository.findByCompanyId(companyId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findByThirdPartiesId(ThirdPartiesId thirdPartiesId, Pageable pageable) {
        if (thirdPartiesId == null || thirdPartiesId.getValue() == null) {
            return Page.empty();
        }
        return contractJpaRepository.findByThirdPartyId(thirdPartiesId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findByStatus(String status, Pageable pageable) {
        return contractJpaRepository.findByStatus(status, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findActiveContracts() {
        return contractJpaRepository.findActiveContracts(Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findExpiringContracts(LocalDate beforeDate, Pageable pageable) {
        return contractJpaRepository.findExpiringBefore(beforeDate, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findExpiredContracts(LocalDate beforeDate) {
        return contractJpaRepository.findExpiredActive(beforeDate, Pageable.unpaged())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsActiveContractForThirdParty(ThirdPartiesId thirdPartiesId) {
        if (thirdPartiesId == null || thirdPartiesId.getValue() == null) {
            return false;
        }
        return contractJpaRepository.existsActiveForThirdParty(thirdPartiesId.getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsActiveContractForCompany(ContractId contractId) {
        // Verifica si hay algún contrato activo para la empresa
        return findById(contractId)
                .map(contract -> contract.isActiveAndValid())
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contract> findAll(Pageable pageable) {
        return contractJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }
}