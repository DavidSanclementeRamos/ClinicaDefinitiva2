package com.example.ClinicaDefinitiva.application.service.adminitration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.*;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport.AdministrativeReportReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport.AdministrativeReportWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting.AdministrativeReportUseCase;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.administration.accounting.output.ReportRepository;
//import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Expense;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AdministrativeReportApplicationService implements AdministrativeReportUseCase {

    private final ReportRepository repository;
    private final JournalEntryRepository journalEntryRepository;
    private final AdministrativeReportReadMapper readMapper;
    private final AdministrativeReportWriteMapper writeMapper;

    public AdministrativeReportApplicationService(ReportRepository repository, JournalEntryRepository journalEntryRepository, AdministrativeReportReadMapper readMapper,AdministrativeReportWriteMapper writeMapper) {
        this.repository = repository;
        this.journalEntryRepository = journalEntryRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }


    @Override
    public ReadAdministrativeReportDto findById(AdministrativeReportId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageAdministrativeReportDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageAdministrativeReportDto> findByPeriod(PeriodDto period, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageAdministrativeReportDto> findByStatus(String status, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public Page<PageAdministrativeReportDto> findByCreator(UserIdentityId creator, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto createReport(CreateAdministrativeReportDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto addJournalEntryReference(AdministrativeReportId id, JournalEntryId entryId, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto removeJournalEntryReference(AdministrativeReportId id, JournalEntryId entryId, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto addIndicator(AdministrativeReportId id, IndicatorDto indicator, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto removeIndicator(AdministrativeReportId id, IndicatorDto indicator, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto addAttachment(AdministrativeReportId id, DocumentDto doc, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto removeAttachment(AdministrativeReportId id, DocumentDto doc, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto updateInformation(AdministrativeReportId id, UpdateAdministrativeReportDto dto, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto submitForReview(AdministrativeReportId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto approve(AdministrativeReportId id, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public ReadAdministrativeReportDto reject(AdministrativeReportId id, String reason, UserIdentityId requesterId, RolId requesterRolId) {
        return null;
    }

    @Override
    public void archive(AdministrativeReportId id, UserIdentityId requesterId, RolId requesterRolId) {

    }

    @Override
    public void unarchive(AdministrativeReportId id, UserIdentityId requesterId, RolId requesterRolId) {

    }
}
