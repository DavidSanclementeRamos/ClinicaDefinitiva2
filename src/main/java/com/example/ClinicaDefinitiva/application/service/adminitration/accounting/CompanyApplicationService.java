package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.company.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.CompanyNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company.CompanyReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.company.CompanyWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.CompanyUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Company;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyStatus;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyApplicationService implements CompanyUseCase {

    private final CompanyReadMapper readMapper;
    private final CompanyWriteMapper writeMapper;
    private final CompanyRepository repository;
    private final AuthorizationHelper authorizationHelper;

    public CompanyApplicationService(CompanyReadMapper readMapper,
                                     CompanyWriteMapper writeMapper,
                                     CompanyRepository repository,
                                     AuthorizationHelper authorizationHelper) {
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.repository = repository;
        this.authorizationHelper = authorizationHelper;
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.READ)
    public ReadCompanyDto findById(CompanyId id,
                                   UserIdentityId requesterId,
                                   RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
         Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));


        return readMapper.toReadDto(company);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageCompanyDto> findAll(Pageable pageable,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageCompanyDto> findByStatus(String status,
                                             Pageable pageable,
                                             UserIdentityId requesterId,
                                             RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageCompanyDto> findByTaxRegime(String regime,
                                                Pageable pageable,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByTaxRegime(regime, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadCompanyDto createCompany(CreateCompanyDto dto,
                                        UserIdentityId requesterId,
                                        RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        Company company = Company.registerCompany(
            writeMapper.toName(dto),
            writeMapper.toNit(dto),
            writeMapper.toTypePerson(dto),
            writeMapper.toTaxRegime(dto),
            writeMapper.toLegalRepresentative(dto),
            writeMapper.toAddress(dto),
            writeMapper.toPhoneNumber(dto),
            writeMapper.toEmail(dto)
        );

        Company saved = repository.save(company);

        return readMapper.toReadDto(saved);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadCompanyDto updateContactInformation(CompanyId id,
                                                   UpdateCompanyContactDto dto,
                                                   UserIdentityId requesterId,
                                                   RolId requesterRolId) {

       
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
         Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));


         company.updateContactInformation(
                             writeMapper.toName(dto),
                    dto.legalRepresentative(),
                           writeMapper.toAddress(dto),
                           writeMapper.toPhoneNumber(dto),
                             writeMapper.toEmail(dto)
             );
        Company updated = repository.save(company);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadCompanyDto updateTaxInformation(CompanyId id,
                                               UpdateCompanyTaxDto dto,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
         Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));


                    company.updateTaxInformation(
    writeMapper.toNit(dto),
    writeMapper.toTaxRegime(dto),
    writeMapper.toTypePerson(dto),
    writeMapper.toIncorporationDate(dto)
);


        Company updated = repository.save(company);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.COMPANY,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadCompanyDto updateStatus(CompanyId id,
                                       CompanyStatus newStatus,
                                       UserIdentityId requesterId,
                                       RolId requesterRolId) {

        

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.COMPANY,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.getValue())
                        .build()
        );
        
         Company company = repository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException("Not found"));


        company.updateStatus(newStatus);
        Company updated = repository.save(company);

        return readMapper.toReadDto(updated);
    }

    @Override
    public void deactivate(CompanyId id, UserIdentityId requesterId, RolId requesterRolId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
}