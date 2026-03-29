package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.entity.CompanyEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.CompanyJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.company.CompanyReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.mapper.company.CompanyWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class CompanyAdapter implements CompanyRepository {

    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyReadEntityMapper readMapper;
    private final CompanyWriteEntityMapper writeMapper;

    public CompanyAdapter(CompanyJpaRepository companyJpaRepository,
                          CompanyReadEntityMapper readMapper,
                          CompanyWriteEntityMapper writeMapper) {
        this.companyJpaRepository = companyJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findById(CompanyId id) {
        if (id == null || id.value() == null) {
            return Optional.empty();
        }
        return companyJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Company> findByNit(String nit) {
        return companyJpaRepository.findByTaxId(nit)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findAll(Pageable pageable) {
        return companyJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findAllStatus(Pageable pageable, CompanyStatus status) {
        return companyJpaRepository.findByStatus(status.getStatus().name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Company save(Company company) {
        if (company == null) return null;

        CompanyEntity entity = writeMapper.toEntity(company);
        CompanyEntity savedEntity = companyJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNit(String nit) {
        return companyJpaRepository.existsByTaxId(nit);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findByTaxRegime(String regime, Pageable pageable) {
        return companyJpaRepository.findByTaxRegime(regime, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Company> findByStatus(String status, Pageable pageable) {
        return companyJpaRepository.findByStatus(status, pageable)
                .map(readMapper::toDomain);
    }
}
