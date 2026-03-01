package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
//import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
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

    Page<Company> findByTaxRegime(String regime, Pageable pageable);

    Page<Company> findByStatus(String status, Pageable pageable);


}
