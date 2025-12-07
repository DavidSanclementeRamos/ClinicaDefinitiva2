package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.ThirdPartiesNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.AddressMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.NameMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.ThirdPartiesMapper;
import com.example.ClinicaDefinitiva.application.mapper.EmailMapper;
import com.example.ClinicaDefinitiva.application.mapper.PhoneNumberMapper;
import com.example.ClinicaDefinitiva.application.usecase.Administration.ThirdPartiesUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.ThirdPartiesRepository;
import org.springframework.data.domain.Page;

public class ThirdPartiesApplicationService implements ThirdPartiesUseCase {
    private final ThirdPartiesRepository repository;
    private final ThirdPartiesMapper mapper;
    private final CompanyRepository companyRepository;

    public ThirdPartiesApplicationService(ThirdPartiesRepository repository, ThirdPartiesMapper mapper, CompanyRepository companyRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public ThirdPartiesResponse registerThirdParty(CreateThirdPartiesRequest request) {
        CompanyId companyId = CompanyId.fromString(request.companyId());
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new CompanyNotFoundException(""));
        ThirdParties thirdParties = ThirdParties.registerThirdParties(
                company.getId(),
                NameMapper.fromDto(request.name()),
                request.typeDocument(),
                request.documentNumber(),
                TypeThirdParties.valueOf(request.typeThirdParties()),
                AddressMapper.fromDto(request.address()),
                PhoneNumberMapper.fromDto(request.phoneNumber()),
                EmailMapper.fromDto(request.email())

        );
        repository.save(thirdParties);

        return mapper.toResponse(thirdParties);
    }

    @Override
    public ThirdPartiesResponse updateThirdPartyContact(String thirdPartiesId, UpdateThirdPartiesContactRequest request) {

        ThirdPartiesId partiesId = ThirdPartiesId.fromString(thirdPartiesId);
        ThirdParties parties = repository.findById(partiesId)
                .orElseThrow(()-> new ThirdPartiesNotFoundException(""));

        parties.updateContactInformation(
                NameMapper.fromDto(request.name()),
                AddressMapper.fromDto(request.address()),
                PhoneNumberMapper.fromDto(request.phoneNumber()),
                EmailMapper.fromDto(request.email())

        );
        repository.save(parties);

        return mapper.toResponse(parties);
    }

    @Override
    public ThirdPartiesResponse activateThirdParty(String thirdPartiesId) {
        ThirdPartiesId partiesId = ThirdPartiesId.fromString(thirdPartiesId);
        ThirdParties parties = repository.findById(partiesId)
                .orElseThrow(()-> new ThirdPartiesNotFoundException(""));

        parties.activate();
        repository.save(parties);

        return mapper.toResponse(parties);
    }

    @Override
    public ThirdPartiesResponse inactivateThirdParty(String thirdPartiesId, InactivateThirdPartiesRequest request) {
        ThirdPartiesId partiesId = ThirdPartiesId.fromString(thirdPartiesId);
        ThirdParties parties = repository.findById(partiesId)
                .orElseThrow(()-> new ThirdPartiesNotFoundException(""));

        parties.inactivate(request.reason());
        repository.save(parties);

        return mapper.toResponse(parties);
    }

    @Override
    public ThirdPartiesResponse findThirdPartyById(String thirdPartiesId) {
        ThirdPartiesId partiesId = ThirdPartiesId.fromString(thirdPartiesId);
        ThirdParties parties = repository.findById(partiesId)
                .orElseThrow(()-> new ThirdPartiesNotFoundException(""));

        return mapper.toResponse(parties);
    }

    @Override
    public ThirdPartiesResponse findThirdPartyByDocument(String documentNumber) {

        ThirdParties parties = repository.findByDocumentNumber(documentNumber)
                .orElseThrow(()-> new ThirdPartiesNotFoundException(""));

        return mapper.toResponse(parties);
    }

    @Override
    public Page<ThirdPartiesListResponse> listThirdPartiesByCompany(String id) {
        CompanyId companyId = CompanyId.fromString(id);
        Page<ThirdParties> thirdPartiesPage = repository.findByCompanyId(companyId);
          if(thirdPartiesPage.isEmpty()){
                throw  new CompanyNotFoundException("");}
        return thirdPartiesPage.map(mapper::toListResponse);
    }

    @Override
    public Page<ThirdPartiesListResponse> listThirdPartiesByType(String type) {
        Page<ThirdParties> thirdPartiesPage = repository.findActiveByType(TypeThirdParties.valueOf(type));
        if(thirdPartiesPage.isEmpty()){
            throw  new CompanyNotFoundException("");}
        return thirdPartiesPage.map(mapper::toListResponse);
    }

    @Override
    public Page<ThirdPartiesListResponse> listActiveThirdPartiesByType(String type) {
        Page<ThirdParties> thirdPartiesPage = repository.findActiveByType(TypeThirdParties.valueOf(type));
        if(thirdPartiesPage.isEmpty()){
            throw  new CompanyNotFoundException("");}
        return thirdPartiesPage.map(mapper::toListResponse);

    }
}
