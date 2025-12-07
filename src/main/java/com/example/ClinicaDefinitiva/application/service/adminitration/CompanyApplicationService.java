package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.AddressMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.CompanyMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.NameMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.NitMapper;
import com.example.ClinicaDefinitiva.application.mapper.EmailMapper;
import com.example.ClinicaDefinitiva.application.mapper.PhoneNumberMapper;
import com.example.ClinicaDefinitiva.application.usecase.Administration.CompanyUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TaxRegime;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyApplicationService implements CompanyUseCase {

    private final CompanyMapper mapper;
    private final CompanyRepository repository;

    public CompanyApplicationService(CompanyMapper mapper, CompanyRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @Override
    public CompanyResponse findId(String id) {
        CompanyId companyId = CompanyId.fromString(String.valueOf(id));
        Company company = repository.findById(companyId)
                .orElseThrow(()-> new CompanyNotFoundException(""));
        return mapper.toResponse(company);
    }

    @Override
    public CompanyResponse findNit(String nit) {
        Company company = repository.findByNit(nit)
                .orElseThrow(()-> new CompanyNotFoundException(""));
        return mapper.toResponse(company);
    }

    @Override
    public Page<CompanyPageResponse> findAll(Pageable pageable) {
        Page<Company> companyPage =  repository.findAll(pageable);
        if(companyPage.isEmpty()){
            throw new CompanyNotFoundException("");
        }
        return companyPage.map(mapper::toListResponse);
    }

    @Override
    public Page<CompanyPageResponse> findAllStatus(Pageable pageable, String status) {

        Page<Company> companyPageStatus = repository.findAllStatus(pageable,CompanyStatus.of(CompanyStatus.Status.valueOf(status)));
        if(companyPageStatus.isEmpty()){
            throw new CompanyNotFoundException("");
        }
        return companyPageStatus.map(mapper::toListResponse);
    }

    @Override
    public CompanyResponse RegisterCompany(CreateCompanyRequest request) {

        if (repository.existsByNit(String.valueOf(request.taxIdentificationNumber()))) {
            throw new DuplicateNitException("Ya existe una compañía con el NIT: " + request.taxIdentificationNumber());
        }

        Company company = Company.registerCompany(
                NameMapper.fromDto(request.name()),
                NitMapper.fromDto(request.taxIdentificationNumber()),
                request.typePerson(),
                TaxRegime.valueOf(String.valueOf(request.taxRegime())),
                request.legalRepresentative(),
                AddressMapper.fromDto( request.address()),
                PhoneNumberMapper.fromDto(request.phoneNumber()),
                EmailMapper.fromDto(request.email())
        );
        repository.save(company);
        return mapper.toResponse(company);
    }



    @Override
    public CompanyResponse updateCompanyContact(String companyId, UpdateCompanyContactRequest request) {
        CompanyId id = CompanyId.fromString(companyId);
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(""));
        company.updateContactInformation(
                NameMapper.fromDto(request.name()),
                request.legalRepresentative(),
                AddressMapper.fromDto( request.address()),
                PhoneNumberMapper.fromDto(request.phoneNumber()),
                EmailMapper.fromDto(request.email())

                );
        repository.save(company);
        return mapper.toResponse(company);
    }

    @Override
    public CompanyResponse updateCompanyTax(String companyId, UpdateCompanyTaxRequest request) {
        CompanyId id = CompanyId.fromString(companyId);
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(""));
        company.updateTaxInformation(
                NitMapper.fromDto(request.taxIdentificationNumber()),
                TaxRegime.valueOf(String.valueOf(request.taxRegime())),
                request.typePerson(),
                request.incorporationDate()
                );
        repository.save(company);
        return mapper.toResponse(company);
    }

    @Override
    public CompanyResponse UpdateCompanyStatus(String companyId, UpdateCompanyStatusRequest request) {
        CompanyId id = CompanyId.fromString(companyId);
        Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(""));
        company.updateStatus(CompanyStatus.of(request.status()));
        return null;
    }


}
