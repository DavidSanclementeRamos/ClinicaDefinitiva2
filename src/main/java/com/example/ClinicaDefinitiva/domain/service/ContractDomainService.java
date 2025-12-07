package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Name;

import java.time.LocalDate;

/**
 * Domain Service: Contract
 * Orquesta reglas de negocio complejas relacionadas con Contract
 */

public class ContractDomainService {


    public static Contract registerContract(
            Company company,
            ThirdParties thirdParties,
            Name name,
            String description,
            String origin,
            LocalDate endDate,
            String coverageType,
            Double coverageRate) {

        if(!company.getStatus().isInactive()) {
            throw new IllegalArgumentException("No se puede crear contrato con un tercero inactivo");
        }

        if (!thirdParties.isActive())  {
            throw new IllegalArgumentException("No se puede crear contrato con un tercero inactivo");
        }

        return  Contract.registerContract(
                company, thirdParties, name, description, origin,
                endDate, coverageType, coverageRate
        );

    }
}