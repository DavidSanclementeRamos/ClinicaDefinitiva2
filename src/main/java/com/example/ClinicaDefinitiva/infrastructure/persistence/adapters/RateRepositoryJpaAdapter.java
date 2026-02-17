package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters;

import org.springframework.stereotype.Component;

@Component
public class RateRepositoryJpaAdapter
        //implements RateRepository
{

    /**private final RateJpaRepository jpa;
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

    }*/
}


