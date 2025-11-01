package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.administration.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.valueObject.ContractId;

import java.util.List;
import java.util.Optional;

public interface ContractRepository {
    Optional<Contract> findById(ContractId contractId);
    List<Contract> findAll();
    Contract update(ContractId id, Contract contract);
    void deleteById(ContractId id);

}
