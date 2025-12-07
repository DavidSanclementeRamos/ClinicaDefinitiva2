package com.example.ClinicaDefinitiva.domain.portsInput.Administration;

import com.example.ClinicaDefinitiva.domain.administration.contable.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> findById(CompanyId id);
    Optional<Company> findByNit( String nit);
    Page<Company> findAll(Pageable pageable);
    Page<Company> findAllStatus(Pageable pageable, CompanyStatus status);
    Company save(Company company);
    boolean existsByNit(String nit);


}
