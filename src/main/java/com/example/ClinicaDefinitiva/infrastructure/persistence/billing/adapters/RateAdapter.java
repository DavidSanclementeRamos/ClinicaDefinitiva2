package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.output.RateRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.RateEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ContractJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository.RateJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.dentalService.jpaRepository.DentalServiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.rate.RateReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.rate.RateWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
public class RateAdapter implements RateRepository {

    private final RateJpaRepository rateJpaRepository;
    private final DentalServiceJpaRepository dentalServiceJpaRepository;
    private final ContractJpaRepository contractJpaRepository;
    private final RateReadEntityMapper readMapper;
    private final RateWriteEntityMapper writeMapper;

    public RateAdapter(
            RateJpaRepository rateJpaRepository,
            DentalServiceJpaRepository dentalServiceJpaRepository,
            ContractJpaRepository contractJpaRepository,
            RateReadEntityMapper readMapper,
            RateWriteEntityMapper writeMapper) {
        this.rateJpaRepository = rateJpaRepository;
        this.dentalServiceJpaRepository = dentalServiceJpaRepository;
        this.contractJpaRepository = contractJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rate> findActiveRateForService(ServiceId serviceId, ContractId contractId) {
        if (serviceId == null || serviceId.getId() == null) {
            return Optional.empty();
        }

        Long contractIdValue = contractId != null ? contractId.getValue() : null;
        LocalDateTime now = LocalDateTime.now();

        return rateJpaRepository.findActiveRateForService(
                        serviceId.getId(),
                        contractIdValue,
                        now
                )
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findAll(Pageable pageable) {
        return rateJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Rate update(RateId id, Rate rate) {
        if (id == null || id.getValue() == null || rate == null) {
            return null;
        }

        if (!rateJpaRepository.existsById(id.getValue())) {
            return null;
        }

        return save(rate);
    }

    @Override
    public Rate save(Rate rate) {
        if (rate == null) return null;

        RateEntity entity = writeMapper.toEntity(rate);

        // Establecer relaciones
        if (rate.getServiceId() != null && rate.getServiceId().getId() != null) {
            dentalServiceJpaRepository.findById(rate.getServiceId().getId())
                    .ifPresent(entity::setDentalService);
        }

        if (rate.getContractId() != null && rate.getContractId().getValue() != null) {
            contractJpaRepository.findById(rate.getContractId().getValue())
                    .ifPresent(entity::setContract);
        }

        RateEntity savedEntity = rateJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(RateId id) {
        if (id != null && id.getValue() != null) {
            rateJpaRepository.deleteById(id.getValue());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Rate> findById(RateId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return rateJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findCurrentlyValid(LocalDateTime now, Pageable pageable) {
        if (now == null) {
            now = LocalDateTime.now();
        }
        return rateJpaRepository.findCurrentlyValid(now, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findByContract(ContractId contractId, Pageable pageable) {
        if (contractId == null || contractId.getValue() == null) {
            return Page.empty();
        }
        return rateJpaRepository.findByContractId(contractId.getValue(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findByService(ServiceId serviceId, Pageable pageable) {
        if (serviceId == null || serviceId.getId() == null) {
            return Page.empty();
        }
        return rateJpaRepository.findByServiceId(serviceId.getId(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Rate> findByPayerType(String payerType, Pageable pageable) {
        if (payerType == null || payerType.isBlank()) {
            return Page.empty();
        }
        return rateJpaRepository.findByPayerType(payerType, pageable)
                .map(readMapper::toDomain);
    }
}