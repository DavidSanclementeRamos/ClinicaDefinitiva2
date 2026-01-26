package com.example.ClinicaDefinitiva.application.portsInput.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyUseCase {
    CompanyResponse findId(String id);
    CompanyResponse findNit(String nit);
    Page<CompanyPageResponse> findAll(Pageable pageable);
    Page<CompanyPageResponse> findAllStatus(Pageable pageable, String status);
    CompanyResponse RegisterCompany(CreateCompanyRequest request);
    CompanyResponse updateCompanyContact(String companyId, UpdateCompanyContactRequest request);
    CompanyResponse updateCompanyTax(String companyId, UpdateCompanyTaxRequest request);
    CompanyResponse UpdateCompanyStatus(String companyId, UpdateCompanyStatusRequest request);

}
