package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.RateId;

import java.util.List;
import java.util.Optional;

public interface RateRepository {
    Optional<Rate> findActiveRateForService(String serviceCode, ContractId contractId);
    Rate findId(RateId id);
    List<Rate> findAll();
    Rate update(RateId id,Rate rate );
    void save(Rate rate);
    void deleteById(RateId id);
}
