package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import com.example.ClinicaDefinitiva.application.dto.shared.AuthorizationContext;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.AdministrativeReportNotFoundException;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.JournalEntryNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport.AdministrativeReportReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport.AdministrativeReportWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.AdministrativeReportUseCase;
import com.example.ClinicaDefinitiva.application.service.shared.AuthorizationHelper;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ReportRepository;
//import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.security.config.RequiresPermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdministrativeReportApplicationService implements AdministrativeReportUseCase {

    private final ReportRepository repository;
    private final JournalEntryRepository journalEntryRepository;
    private final AdministrativeReportReadMapper readMapper;
    private final AdministrativeReportWriteMapper writeMapper;
    private final AuthorizationHelper authorizationHelper;

    public AdministrativeReportApplicationService(ReportRepository repository,
                                                   JournalEntryRepository journalEntryRepository,
                                                   AdministrativeReportReadMapper readMapper,
                                                   AdministrativeReportWriteMapper writeMapper,
                                                   AuthorizationHelper authorizationHelper) {
        this.repository = repository;
        this.journalEntryRepository = journalEntryRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
        this.authorizationHelper = authorizationHelper;
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.READ)
    public ReadAdministrativeReportDto findById(AdministrativeReportId id,
                                                UserIdentityId requesterId,
                                                RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy()) // Creador ve su propio reporte
                        .build()
        );

        return readMapper.toReadDto(report);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageAdministrativeReportDto> findAll(Pageable pageable,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findAll(pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageAdministrativeReportDto> findByPeriod(PeriodDto period,
                                                          Pageable pageable,
                                                          UserIdentityId requesterId,
                                                          RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByPeriod(period.star(), period.end(), pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageAdministrativeReportDto> findByStatus(String status,
                                                          Pageable pageable,
                                                          UserIdentityId requesterId,
                                                          RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder().build()
        );

        return repository.findByStatus(status, pageable)
                .map(readMapper::toPageDto);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.READ)
    public Page<PageAdministrativeReportDto> findByCreator(UserIdentityId creator,
                                                           Pageable pageable,
                                                           UserIdentityId requesterId,
                                                           RolId requesterRolId) {

        // OwnershipPolicy: el creador solo puede ver los suyos; administrador ve todos
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.READ,
                AuthorizationContext.builder()
                        .withOwnership(creator)
                        .build()
        );

        return repository.findByCreator(creator, pageable)
                .map(readMapper::toPageDto);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.CREATE)
    public ReadAdministrativeReportDto createReport(CreateAdministrativeReportDto dto,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.CREATE,
                AuthorizationContext.builder().build()
        );

        AdministrativeReport report = AdministrativeReport.create(
                writeMapper.toName(dto),
                writeMapper.toPeriod(dto),
                writeMapper.toUserIdentityId(dto)
        );
        AdministrativeReport saved = repository.save(report);

        return readMapper.toReadDto(saved);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto addJournalEntryReference(AdministrativeReportId id,
                                                                JournalEntryId entryId,
                                                                UserIdentityId requesterId,
                                                                RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        journalEntryRepository.findById(entryId)
                .orElseThrow(() -> new JournalEntryNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.addJournalEntryReference(entryId);
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto removeJournalEntryReference(AdministrativeReportId id,
                                                                   JournalEntryId entryId,
                                                                   UserIdentityId requesterId,
                                                                   RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.removeJournalEntryReference(entryId);
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto addIndicator(AdministrativeReportId id,
                                                    IndicatorDto indicator,
                                                    UserIdentityId requesterId,
                                                    RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.addIndicator(writeMapper.toIndicator(indicator));
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto removeIndicator(AdministrativeReportId id,
                                                       IndicatorDto indicator,
                                                       UserIdentityId requesterId,
                                                       RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.removeIndicator(writeMapper.toIndicator(indicator));
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto addAttachment(AdministrativeReportId id,
                                                     DocumentDto doc,
                                                     UserIdentityId requesterId,
                                                     RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.addAttachment(writeMapper.toDocument(doc));
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto removeAttachment(AdministrativeReportId id,
                                                        DocumentDto dto,
                                                        UserIdentityId requesterId,
                                                        RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.addAttachment(writeMapper.toDocument(dto));
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto updateInformation(AdministrativeReportId id,
                                                         UpdateAdministrativeReportDto dto,
                                                         UserIdentityId requesterId,
                                                         RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

             report.updateInformation(
                     writeMapper.toName(dto),
                     writeMapper.toNotes(dto)
);
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }


    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public ReadAdministrativeReportDto submitForReview(AdministrativeReportId id,
                                                       UserIdentityId requesterId,
                                                       RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        // Solo el creador puede enviar a revisión
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .withOwnership(report.getCreatedBy())
                        .build()
        );

        report.submitForReview();
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.APPROVE)
    public ReadAdministrativeReportDto approve(AdministrativeReportId id,
                                               UserIdentityId requesterId,
                                               RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        // SectorBasedPolicy: solo roles con permiso de aprobación (ej. supervisor)
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.APPROVE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .build()
        );

        report.approve(requesterId);
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.APPROVE)
    public ReadAdministrativeReportDto reject(AdministrativeReportId id,
                                              String reason,
                                              UserIdentityId requesterId,
                                              RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        // SectorBasedPolicy: mismo rol que aprueba puede rechazar
        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.APPROVE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .build()
        );

        report.reject(reason);
        AdministrativeReport updated = repository.save(report);

        return readMapper.toReadDto(updated);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void archive(AdministrativeReportId id,
                        UserIdentityId requesterId,
                        RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .build()
        );

        report.archive();
        repository.save(report);
    }

    @Override
    @RequiresPermission(resource = ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
            action = ActionCatalog.BasicAction.UPDATE)
    public void unarchive(AdministrativeReportId id,
                          UserIdentityId requesterId,
                          RolId requesterRolId) {

        AdministrativeReport report = repository.findById(id)
                .orElseThrow(() -> new AdministrativeReportNotFoundException("Not found"));

        authorizationHelper.authorize(
                requesterId, requesterRolId,
                ResourceCatalog.BasicResource.ADMINISTRATIVE_REPORT,
                ActionCatalog.BasicAction.UPDATE,
                AuthorizationContext.builder()
                        .withResourceId(id.value())
                        .build()
        );

        report.unarchive();
        repository.save(report);
    }
}