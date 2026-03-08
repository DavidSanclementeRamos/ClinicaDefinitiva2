package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.CreateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.DocumentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.IndicatorDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.PageAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.PeriodDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.ReadAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.dto.administration.accounting.administrativeReport.UpdateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.AdministrativeReportId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrativeReportUseCase {


    ReadAdministrativeReportDto findById(
            AdministrativeReportId id,
            UserIdentityId requesterId,
            RolId requesterRolId);

    Page<PageAdministrativeReportDto> findAll(
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId);

    Page<PageAdministrativeReportDto> findByPeriod(
            PeriodDto period,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId);

    Page<PageAdministrativeReportDto> findByStatus(
            String status,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId);

    Page<PageAdministrativeReportDto> findByCreator(
            UserIdentityId creator,
            Pageable pageable,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto createReport(
            CreateAdministrativeReportDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto addJournalEntryReference(
            AdministrativeReportId id,
            JournalEntryId entryId,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto removeJournalEntryReference(
            AdministrativeReportId id,
            JournalEntryId entryId,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto addIndicator(
            AdministrativeReportId id,
            IndicatorDto indicator,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto removeIndicator(
            AdministrativeReportId id,
            IndicatorDto indicator,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto addAttachment(
            AdministrativeReportId id,
            DocumentDto doc,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto removeAttachment(
            AdministrativeReportId id,
            DocumentDto doc,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto updateInformation(
            AdministrativeReportId id,
            UpdateAdministrativeReportDto dto,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto submitForReview(
            AdministrativeReportId id,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto approve(
            AdministrativeReportId id,
            UserIdentityId requesterId,
            RolId requesterRolId);

    ReadAdministrativeReportDto reject(
            AdministrativeReportId id,
            String reason,
            UserIdentityId requesterId,
            RolId requesterRolId);

    void archive(
            AdministrativeReportId id,
            UserIdentityId requesterId,
            RolId requesterRolId);

    void unarchive(
            AdministrativeReportId id,
            UserIdentityId requesterId,
            RolId requesterRolId);

}
