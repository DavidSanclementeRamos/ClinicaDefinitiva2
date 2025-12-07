package com.example.ClinicaDefinitiva.infrastructure.adapters;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;
import com.example.ClinicaDefinitiva.domain.portsInput.RateRepository;
import com.example.ClinicaDefinitiva.infrastructure.repository.RateJpaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class RateRepositoryJpaAdapter implements RateRepository {

    private final RateJpaRepository jpa;
    private final RateEntityMapper mapper;

    public RateRepositoryJpaAdapter(RateJpaRepository jpa, RateEntityMapper mapper) {
        this.jpa = jpa; this.mapper = mapper;
    }


    @Override
    public Optional<Rate> findActiveRateForService(String serviceCode, ContractId contractId) {
        return jpa.findActiveForService(serviceCode, contractId, LocalDateTime.now()).map(mapper::toDomain);
    }

    @Override
    public Rate findId(RateId id) {
        return null;
    }

    @Override
    public List<Rate> findAll() {
        return List.of();
    }

    @Override
    public Rate update(RateId id, Rate rate) {
        return null;
    }

    @Override
    public void save(Rate rate) {

    }

    @Override
    public void deleteById(RateId id) {

    }
}


